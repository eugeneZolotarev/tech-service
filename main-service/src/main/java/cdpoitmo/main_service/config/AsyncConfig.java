package cdpoitmo.main_service.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;

@Configuration
@Slf4j
public class AsyncConfig implements AsyncConfigurer {
    @Bean(name = "emailExecutor")
    @Override
    public Executor getAsyncExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();

        pool.setCorePoolSize(1);
        pool.setMaxPoolSize(5);
        pool.setQueueCapacity(100);

        pool.setThreadNamePrefix("EmailThread-");

        pool.initialize();
        return pool;
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return (Throwable ex, Method method, Object... params) ->{
            log.atError().log("Ошибка в асинхронном методе: " + method.getName());
            log.atError().log("Текст ошибки: " + ex.getMessage());
        };
    }
}
