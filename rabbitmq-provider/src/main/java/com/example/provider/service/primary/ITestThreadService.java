package com.example.provider.service.primary;

import java.util.concurrent.ExecutionException;

public interface ITestThreadService {

    void testThreadSafe() throws InterruptedException, ExecutionException;

}
