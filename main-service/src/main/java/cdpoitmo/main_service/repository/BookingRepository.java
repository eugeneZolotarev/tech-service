package cdpoitmo.main_service.repository;

import cdpoitmo.main_service.constants.BookingStatus;
import cdpoitmo.main_service.entity.Booking;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

public interface BookingRepository {
    List<Booking> findByAppointmentTime(LocalDateTime appointmentTime);
    List<Booking> findByStatus(BookingStatus status);

    @Query("SELECT CAST(b.appointmentTime AS date) as date, SUM(b.finalPrice) as revenue " +
            "FROM Booking b " +
            "WHERE b.status = :status " +
            "AND b.appointmentTime BETWEEN :startDate AND :endDate " +
            "GROUP BY CAST(b.appointmentTime AS date)")
    List<Object[]> getStatisticReport(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate,
            @Param("status") BookingStatus status
    );
}
