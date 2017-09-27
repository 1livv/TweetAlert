package com.livv.TwitterAlert;

import org.apache.log4j.Logger;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by gheorghe on 27/09/2017.
 */
public class NotificationPool extends ThreadPoolExecutor {


    private int noThreads;

    private int queueSize;

    private long timeout;

    private static Logger log = Logger.getLogger(NotificationPool.class);

    public NotificationPool(int noThreads, int queueSize, long timeout) {
        super(noThreads, noThreads, 1, TimeUnit.MINUTES, new ArrayBlockingQueue<Runnable>(queueSize));
        this.noThreads = noThreads;
        this.queueSize = queueSize;
        this.timeout = timeout;
    }

    public int getNoThreads() {
        return noThreads;
    }

    public int getQueueSize() {
        return queueSize;
    }

    @Override
    public void beforeExecute(Thread t, Runnable r) {
        log.info("executing thread " + t.getId());
        super.beforeExecute(t, r);
    }

    @Override
    protected void afterExecute(Runnable t, Throwable throwable) {
        log.info("finished thread  with status " + throwable.getMessage());
        super.afterExecute(t, throwable);
    }

    @Override
    public void shutdown() {
        log.info("Request to shutdown the pool");
        boolean result = true;
        try {
            result = awaitTermination(timeout, TimeUnit.MILLISECONDS);
        }
        catch(InterruptedException e) {
            result = false;
        }

        if (!result) {
            log.error("did not shutdown gracefully " + getQueueSize() +" threads did not finish");
        }
    }
}
