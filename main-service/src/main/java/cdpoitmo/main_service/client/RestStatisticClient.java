package cdpoitmo.main_service.client;

import cdpoitmo.main_service.dto.BookingStatisticDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.List;

@Profile("rest")
@Component
public class RestStatisticClient implements StatisticClient{

    private final RestClient restClient;

    public RestStatisticClient(@Value("${dwh.service.url}") String dwhUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(dwhUrl)
                .build();
    }

    @Override
    public List<Long> sendStatistics(List<BookingStatisticDTO> statistics) {
        try {
            return restClient.post()
                    .uri("/api/stat/batch")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(statistics)
                    .retrieve()
                    .body(new ParameterizedTypeReference<List<Long>>() {});
        } catch (Exception e) {
            return List.of();
        }
    }
}
