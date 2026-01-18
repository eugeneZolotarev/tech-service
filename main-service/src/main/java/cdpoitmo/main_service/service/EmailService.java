package cdpoitmo.main_service.service;

import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Booking;

public interface EmailService {
    void sendNotificationCreatedBooking(ApplicationUser user, Booking booking);
    void sendNotificationUpdateTime(ApplicationUser user, Booking booking);
    void sendNotificationCancelledByUser(ApplicationUser user, Booking booking);
    void sendNotificationCancelledByAdmin(ApplicationUser user, Booking booking);
}
