package com.example.provider.config.thread;

import com.example.provider.exception.BasicException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.Assert;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义线程工厂类
 *
 * @Author songbo
 * @Date 2020/8/13 14:40
 * @Version 1.0
 */
@Slf4j
public class CustomThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger atomicInteger = new AtomicInteger(1);
    private final String prefix;

    public CustomThreadFactory(String prefix) {
        Assert.notNull(prefix, "线程名前缀不可为空！");
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.prefix = prefix;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, prefix + atomicInteger.getAndIncrement(), 0);
        // 守护线程
        if (t.isDaemon()) {
            t.setDaemon(true);
        }

        // 线程优先级
        if (t.getPriority() != Thread.NORM_PRIORITY) {
            t.setPriority(Thread.NORM_PRIORITY);
        }

        // 处理未捕捉的异常
        t.setUncaughtExceptionHandler((t1, e) -> log.error(BasicException.exceptionTrace(e)));
        return t;
    }
}
