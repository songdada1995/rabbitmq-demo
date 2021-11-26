package com.example.provider.test;

import com.example.provider.RabbitMqProviderApplication;
import com.example.provider.service.primary.ITestThreadService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import javax.annotation.Resource;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = RabbitMqProviderApplication.class)
@WebAppConfiguration
public class RabbitMqProviderTest {

    @Resource
    private ITestThreadService testThreadService;

    @Test
    public void test1() {
        try {
            testThreadService.testThreadSafe();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
