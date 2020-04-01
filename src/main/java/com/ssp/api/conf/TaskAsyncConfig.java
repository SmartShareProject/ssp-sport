package com.ssp.api.conf;
//package com.rnjf.recycle.conf;
//
//import org.apache.tomcat.util.threads.ThreadPoolExecutor;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableAsync;
//import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
//
//import java.util.concurrent.Executor;
//
///**
// * Created by yangxiaopeng on 2017/9/25.
// */
//@Configuration
//@ComponentScan("com.rnjf.recycle")
//@EnableAsync
//public class TaskAsyncConfig {
//    /**最小线程数*/
//    private int corePoolSize = 10;
//    /**最大线程数*/
//    private int maxPoolSize = 100;
//    /** 队列容量*/
//    private int queueCapacity = 20;
//
//    private String ThreadNamePrefix = "taskAsync";
//
//    @Bean
//    public Executor taskAsync() {
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(corePoolSize);
//        executor.setMaxPoolSize(maxPoolSize);
//        executor.setQueueCapacity(queueCapacity);
//        executor.setThreadNamePrefix(ThreadNamePrefix);
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
//        return executor;
//    }
//
//
//}
