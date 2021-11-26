package com.example.consumer.test;

import com.example.consumer.RabbitMqConsumer1Application;
import com.example.consumer.task.TestAsyncThreadPoolTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitMqConsumer1Application.class)
@WebAppConfiguration
public class AsyncThreadPoolTest {

    @Resource
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Test
    public void testPool() {
        try {
            for (int i = 1; i <= 100; i++) {
                TestAsyncThreadPoolTask testAsyncThread = new TestAsyncThreadPoolTask(i);
                threadPoolTaskExecutor.execute(testAsyncThread);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
