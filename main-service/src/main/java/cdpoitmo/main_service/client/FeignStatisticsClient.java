package cdpoitmo.main_service.client;

import cdpoitmo.main_service.dto.BookingStatisticDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Profile("feign")
@Component
@RequiredArgsConstructor
public class FeignStatisticsClient implements StatisticClient{

    private final DwhFeignClient dwhFeignClient;

    @Override
    public List<Long> sendStatistics(List<BookingStatisticDTO> statistics) {
        return dwhFeignClient.sendBatch(statistics);
    }
}
