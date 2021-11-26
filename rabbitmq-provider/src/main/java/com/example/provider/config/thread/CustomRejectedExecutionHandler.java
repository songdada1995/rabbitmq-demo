package com.example.provider.config.thread;

import com.example.provider.exception.BasicException;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author songbo
 * @Date 2020/8/13 15:52
 * @Version 1.0
 */
@Slf4j
public class CustomRejectedExecutionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        if (!executor.isShutdown()) {
            try {
                log.info("start get queue");
                executor.getQueue().put(r);
                log.info("end get queue");
            } catch (InterruptedException e) {
                log.error(BasicException.exceptionTrace(e));
                Thread.currentThread().interrupt();
            }
        }
    }

}
