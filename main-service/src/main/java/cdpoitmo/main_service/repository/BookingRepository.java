package cdpoitmo.main_service.repository;

import cdpoitmo.main_service.constants.BookingStatus;
import cdpoitmo.main_service.dto.bookingDTO.RevenueDTO;
import cdpoitmo.main_service.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // все записи юзера по его айди
    List<Booking> findAllByUserIdOrderByAppointmentTimeDesc(Long userId);

    // все записи с заданным статусом и айди юзера
    List<Booking> findAllByUserIdAndStatusOrderByAppointmentTimeDesc(Long userId, BookingStatus status);

    //найти в интервале дат - это для админа завели
    List<Booking> findAllByAppointmentTimeBetween(LocalDateTime start, LocalDateTime end);

    //Возвращает все брони, которые не были занесены еще в сервис сбора статистики
    List<Booking> findAllByStatusAndIsSynchedFalse(BookingStatus status);

    @Query("SELECT new cdpoitmo.main_service.dto.bookingDTO.RevenueDTO(CAST(b.appointmentTime AS LocalDate), SUM(b.finalPrice)) " +
            "FROM Booking b " +
            "WHERE b.status = 'COMPLETED' AND b.appointmentTime BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(b.appointmentTime AS LocalDate)")
    List<RevenueDTO> getStatisticReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}
