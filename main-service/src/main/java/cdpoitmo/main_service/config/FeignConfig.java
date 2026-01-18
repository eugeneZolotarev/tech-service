package cdpoitmo.main_service.config;

import feign.RetryableException;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    public Retryer retryer() {
        return new Retryer.Default(1000, 2000, 5);
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return (methodKey, response) -> {
            if (response.status() >= 500) {
                return new RetryableException(
                        response.status(),
                        "Сервер DWH недоступен",
                        response.request().httpMethod(),
                        (Long) null,
                        response.request()
                );
            }
            return new ErrorDecoder.Default().decode(methodKey, response);
        };
    }
}
