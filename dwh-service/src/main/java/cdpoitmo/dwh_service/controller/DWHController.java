package cdpoitmo.dwh_service.controller;

import cdpoitmo.dwh_service.DTO.BookingStatisticDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@RestController
@RequestMapping("/api/stat")
@RequiredArgsConstructor
@Slf4j
public class DWHController {

    @PostMapping("/batch")
    public List<Long> acceptBatch(@RequestBody List<BookingStatisticDTO> stats) {

        List<Long> successIds = new ArrayList<>();
        Random random = new Random();

        //80% успешных запросов
        for (BookingStatisticDTO stat : stats) {
            if (random.nextInt(10) > 1) successIds.add(stat.bookingId());
        }

        return successIds;
    }
}
