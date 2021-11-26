package com.example.provider.service.primary.impl;

import com.example.provider.dao.primary.TAccountMapper;
import com.example.provider.dao.primary.TOrderMapper;
import com.example.provider.exception.BasicException;
import com.example.provider.model.primary.TAccount;
import com.example.provider.model.primary.TOrder;
import com.example.provider.service.primary.ITestThreadService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class TestThreadServiceImpl implements ITestThreadService {

    private static AtomicInteger count = new AtomicInteger(0);

    @Resource
    private TAccountMapper tAccountMapper;
    @Resource
    private TOrderMapper tOrderMapper;
    @Resource(name = "coreThreadPoolExecutor")
    private ThreadPoolExecutor threadPoolExecutor;
    @Resource(name = "primaryTransactionManager")
    private DataSourceTransactionManager transactionManager;

    @Override
    public void testThreadSafe() throws InterruptedException, ExecutionException {
        List<TransactionStatus> transactionStatusList = Collections.synchronizedList(new ArrayList<>());
        try {

            log.info("开始执行修改账号表");
            DefaultTransactionDefinition def = new DefaultTransactionDefinition(); // 事务定义类
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            TransactionStatus mainTransactionStatus = transactionManager.getTransaction(def); // 返回事务对象
            TAccount tAccount = new TAccount();
            tAccount.setId(1);
            tAccount.setAccountType("2");
            tAccountMapper.updateByPrimaryKeySelective(tAccount);
            transactionManager.commit(mainTransactionStatus);
            transactionStatusList.add(mainTransactionStatus);
            log.info("结束执行修改账号表");


            List<Future<Boolean>> futureList = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(10);
            for (int i = 0; i < 10; i++) {
                Future<Boolean> future = threadPoolExecutor.submit(() -> executeTask(transactionStatusList, latch));
                futureList.add(future);
            }
            latch.await();

            if (CollectionUtils.isNotEmpty(futureList)) {
                for (Future<Boolean> future : futureList) {
                    future.get();
                }
            }


        } catch (Exception e) {
            log.error(BasicException.exceptionTrace(e));
            for (TransactionStatus transactionStatus : transactionStatusList) {
                transactionStatus.setRollbackOnly();
            }
        }

    }

    public Boolean executeTask(List<TransactionStatus> transactionStatusList, CountDownLatch latch) throws InterruptedException {
        TransactionStatus transactionStatus = null;
        try {
            DefaultTransactionDefinition def = new DefaultTransactionDefinition();
            def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
            transactionStatus = transactionManager.getTransaction(def);

            // 写入一条数据
            int increment = count.incrementAndGet();
            TOrder tOrder = new TOrder();
            tOrder.setOrderNo("NMD-" + increment);
            tOrder.setStock(increment);
            tOrderMapper.insert(tOrder);

            Thread.sleep(1 * 1000);

            if (increment == 9) {
                throw new RuntimeException("异常");
            }

            transactionManager.commit(transactionStatus);

            return true;
        } catch (Exception e) {
            log.error(BasicException.exceptionTrace(e));
            throw e;
        } finally {
            latch.countDown();
            transactionStatusList.add(transactionStatus);
        }
    }

}
