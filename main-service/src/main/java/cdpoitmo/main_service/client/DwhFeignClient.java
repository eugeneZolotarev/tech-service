package cdpoitmo.main_service.client;

import cdpoitmo.main_service.config.FeignConfig;
import cdpoitmo.main_service.dto.BookingStatisticDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Profile(value = "feign")
@FeignClient(name = "dwh-client", url = "${dwh.service.url}", configuration = FeignConfig.class)
public interface DwhFeignClient {
    @PostMapping("/api/stat/batch")
    List<Long> sendBatch(@RequestBody List<BookingStatisticDTO> stats);
}
