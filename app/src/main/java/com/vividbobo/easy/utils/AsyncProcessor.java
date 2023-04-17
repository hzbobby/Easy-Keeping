package com.vividbobo.easy.utils;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.vividbobo.easy.ui.account.AccountPicker;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class AsyncProcessor {
    private static final int THREAD_NUM = 3;
    private static volatile AsyncProcessor instance;
    private final ListeningExecutorService executorService;

    public AsyncProcessor() {
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(THREAD_NUM));
    }

    public static AsyncProcessor getInstance() {
        if (instance == null) {
            synchronized (AsyncProcessor.class) {
                if (instance == null) {
                    instance = new AsyncProcessor();
                }
            }
        }
        return instance;
    }

    public <T> ListenableFuture<T> submit(Callable<T> task) {
        return executorService.submit(task);
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
