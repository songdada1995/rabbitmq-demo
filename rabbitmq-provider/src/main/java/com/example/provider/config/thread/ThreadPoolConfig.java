package com.example.provider.config.thread;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @Author songbo
 * @Date 2020/8/13 12:22
 * @Version 1.0
 */
@Configuration
public class ThreadPoolConfig {

    @Resource
    private ThreadPoolProperties threadPoolProperties;

    @Bean
    public ThreadPoolExecutor coreThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                threadPoolProperties.getCorePoolSize(),
                threadPoolProperties.getMaximumPoolSize(),
                threadPoolProperties.getKeepAliveTime(),
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(threadPoolProperties.getCapacity()),
                new CustomThreadFactory("CoreThreadPoolExecutor-"),
                new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

}
