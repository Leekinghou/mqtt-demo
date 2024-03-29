package com.ljh.mqttdemo.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

/**
 * @author lijinhao
 * @version 1.0
 * @date 2022/12/20 15:42
 */

@Configuration
public class GlobalThreadPoolConfiguration {
    @Value("${thread.pool.core-pool-size: 10}")
    private int corePoolSize;

    @Value("${thread.pool.maximum-pool-size: 20}")
    private int maximumPoolSize;

    @Value("${thread.pool.keep-alive-time: 60}")
    private long keepAliveTime;

    @Value("${thread.pool.queue.capacity: 1000}")
    private int capacity;

    /**
     * A custom thread pool.
     * @return
     */
    @Bean
    public Executor threadPool() {
        return new ThreadPoolExecutor(corePoolSize,
                maximumPoolSize, keepAliveTime,
                TimeUnit.SECONDS, new LinkedBlockingQueue<>(capacity),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardOldestPolicy());
    }
}
