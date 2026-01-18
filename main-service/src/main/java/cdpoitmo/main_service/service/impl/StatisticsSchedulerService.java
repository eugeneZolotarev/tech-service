package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.client.StatisticClient;
import cdpoitmo.main_service.constants.BookingStatus;
import cdpoitmo.main_service.dto.BookingStatisticDTO;
import cdpoitmo.main_service.entity.Booking;
import cdpoitmo.main_service.repository.BookingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StatisticsSchedulerService {

    private final StatisticClient statisticsClient;
    private final BookingRepository bookingRepository;

    @Scheduled(fixedDelay = 60000)
    @Transactional
    public void sendStatistic() {
        List<Booking> unsyncedBookings = bookingRepository.findAllByStatusAndIsSynchedFalse(BookingStatus.COMPLETED);

        if (unsyncedBookings.isEmpty()) return;

        List<BookingStatisticDTO> dtoList = unsyncedBookings.stream()
                .map(b -> new BookingStatisticDTO(
                        b.getId(),
                        b.getUser().getId(),
                        b.getTechServiceEntity().getName(),
                        b.getFinalPrice(),
                        b.getAppointmentTime()
                ))
                .toList();

        List<Long> successIds = statisticsClient.sendStatistics(dtoList);

        //если со стороны клиента пришел пустой список
        if (successIds == null || successIds.isEmpty()) return;

        List<Booking> syncedBookings = unsyncedBookings.stream()
                .filter(b -> successIds.contains(b.getId()))
                .toList();

        syncedBookings.forEach(b -> b.setIsSynced(true));
        bookingRepository.saveAll(syncedBookings);
    }
}
