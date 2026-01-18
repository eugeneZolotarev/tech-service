package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Booking;
import cdpoitmo.main_service.service.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@Profile("dev")
public class EmailServiceImplDev implements EmailService {

    @Override
    public void sendNotificationCreatedBooking(ApplicationUser user, Booking booking) {
        log.info("MOCK EMAIL [Created]: Кому: {}, Текст: Бронь на {} создана",
                user.getEmail(), booking.getTechServiceEntity().getName());
    }

    @Override
    public void sendNotificationUpdateTime(ApplicationUser user, Booking booking) {
        log.info("MOCK EMAIL [Update]: Кому: {}, Новое время: {}",
                user.getEmail(), booking.getAppointmentTime());
    }

    @Override
    public void sendNotificationCancelledByUser(ApplicationUser user, Booking booking) {
        log.info("MOCK EMAIL [Cancel-User]: Бронь {} отменена пользователем", booking.getId());
    }

    @Override
    public void sendNotificationCancelledByAdmin(ApplicationUser user, Booking booking) {
        log.info("MOCK EMAIL [Cancel-Admin]: Бронь {} отменена администратором", booking.getId());
    }
}
