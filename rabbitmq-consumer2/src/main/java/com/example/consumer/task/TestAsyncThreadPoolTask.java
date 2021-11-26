package com.example.consumer.task;

public class TestAsyncThreadPoolTask implements Runnable {

    private int count;

    public TestAsyncThreadPoolTask(int count) {
        this.count = count;
    }

    @Override
    public void run() {
        System.out.println(count);
    }
}
