package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Booking;
import cdpoitmo.main_service.service.EmailService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
@Profile("prod")
public class EmailServiceImplProd implements EmailService{

    private final JavaMailSender javaMailSender;

    @Value("${app.email.adress.from}")
    private String fromAddress;

    @Async("emailExecutor")
    public void sendMessage(String to, String subject, String text) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "UTF-8");
        try {
            mimeMessageHelper.setFrom(fromAddress);
            mimeMessageHelper.setTo(to);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setText(text, false);
            javaMailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.error("Failed to send email to {}", to, e);
        }
    }

    @Override
    public void sendNotificationCreatedBooking(ApplicationUser user, Booking booking) {
        String subject = "Бронь была подтверждена";
        String text = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Ваша бронь на услугу '%s' была успешно создана.\n" +
                        "Назначенное время: %s\n" +
                        "Итоговая стоимость: %s руб.",
                user.getUsername(),
                booking.getTechServiceEntity().getName(),
                booking.getAppointmentTime(),
                booking.getFinalPrice()
        );
        sendMessage(user.getEmail(), subject, text);
    }

    @Override
    public void sendNotificationUpdateTime(ApplicationUser user, Booking booking) {
        String subject = "Время бронирования было изменено";
        String text = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Ваша бронь на услугу '%s' была успешно создана.\n" +
                        "Назначенное время: %s\n" +
                        "Итоговая стоимость: %s руб.",
                user.getUsername(),
                booking.getTechServiceEntity().getName(),
                booking.getAppointmentTime(),
                booking.getFinalPrice()
        );
        sendMessage(user.getEmail(), subject, text);
    }

    @Override
    public void sendNotificationCancelledByUser(ApplicationUser user, Booking booking) {
        String subject = "Отмена бронирования";
        String text = String.format(
                "Здравствуйте, %s!\n\n" +
                        "Ваша бронь на услугу '%s' (%s) была отменена по вашему запросу.",
                user.getUsername(),
                booking.getTechServiceEntity().getName(),
                booking.getAppointmentTime()
        );
        sendMessage(user.getEmail(), subject, text);
    }

    @Override
    public void sendNotificationCancelledByAdmin(ApplicationUser user, Booking booking) {
        String subject = "Отмена записи сервисом";
        String text = String.format(
                "Здравствуйте, %s!\n\n" +
                        "К сожалению, мы вынуждены отменить вашу запись на '%s' (%s) по техническим причинам.\n\n" +
                        "Приносим свои искренние извинения!\n" +
                        "В качестве компенсации мы дарим вам скидку 15%% на следующий заказ.\n" +
                        "Скидка будет автоматически применена при следующем бронировании",
                user.getUsername(),
                booking.getTechServiceEntity().getName(),
                booking.getAppointmentTime()
        );
        sendMessage(user.getEmail(), subject, text);
    }
}
