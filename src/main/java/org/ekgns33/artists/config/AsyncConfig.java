package org.ekgns33.artists.config;

import java.util.Arrays;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@Configuration
@EnableAsync
@EnableRetry
public class AsyncConfig implements AsyncConfigurer {

    @Bean("kopisExecutor")
    public Executor kopisExecutor()
     {
        ThreadFactory virtualThreadFactory =
            Thread.ofVirtual()
                .factory();
        var executor = new ThreadPoolTaskExecutor() {
            @Override
            public void execute(Runnable command) {
                virtualThreadFactory.newThread(command).start();
            }
        };
        executor.setThreadNamePrefix("V-Detail-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        return executor;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (ex, method, params) ->
            log.error("Async failure in {} with {}", method,    Arrays.toString(params), ex);
    }
}