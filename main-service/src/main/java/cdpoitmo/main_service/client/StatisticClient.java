package cdpoitmo.main_service.client;

import cdpoitmo.main_service.dto.BookingStatisticDTO;

import java.util.List;

public interface StatisticClient {
    //Единый интерфейс для RestClient и FeignClient
    List<Long> sendStatistics(List<BookingStatisticDTO> statistics);
}
