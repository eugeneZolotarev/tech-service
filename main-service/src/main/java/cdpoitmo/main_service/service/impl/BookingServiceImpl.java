package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.constants.BookingStatus;
import cdpoitmo.main_service.dto.bookingDTO.BookingRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Booking;
import cdpoitmo.main_service.entity.TechServiceEntity;
import cdpoitmo.main_service.exception.AccessDeniedException;
import cdpoitmo.main_service.exception.BusinessValidationException;
import cdpoitmo.main_service.exception.ResourceNotFoundException;
import cdpoitmo.main_service.mapper.BookingMapper;
import cdpoitmo.main_service.repository.BookingRepository;
import cdpoitmo.main_service.repository.TechServiceRepository;
import cdpoitmo.main_service.repository.UserRepository;
import cdpoitmo.main_service.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final TechServiceRepository techServiceRepository;
    private final EmailService emailService;

    @Transactional
    @Override
    public BookingResponseDTO createBooking(BookingRequestDTO bookingRequestDTO, String username) {
        ApplicationUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не был найден"));

        TechServiceEntity service = techServiceRepository.findById(bookingRequestDTO.serviceId())
                .orElseThrow(() -> new ResourceNotFoundException("Бронь с таким id=" + bookingRequestDTO.serviceId() + " не существует"));

        Double personalDiscount = user.getPersonalDiscount() != null ? user.getPersonalDiscount() : 0.0;

        BigDecimal originalServicePrice = service.getPrice();
        BigDecimal finalPrice = originalServicePrice;

        if (personalDiscount > 0.) {
            BigDecimal discountFactor = BigDecimal.valueOf(100 - personalDiscount).divide(BigDecimal.valueOf(100));
            finalPrice = originalServicePrice.multiply(discountFactor).setScale(2, RoundingMode.HALF_UP);
            user.setPersonalDiscount(0.0);
        }

        Booking booking = Booking.builder()
                .user(user)
                .techServiceEntity(service)
                .appointmentTime(bookingRequestDTO.appointmentTime())
                .status(BookingStatus.CREATED)
                .finalPrice(finalPrice)
                .discountPercent(personalDiscount)
                .build();

        bookingRepository.save(booking);

        emailService.sendNotificationCreatedBooking(user, booking);

        return bookingMapper.toDTO(booking);
    }
    
    @Override
    @Transactional
    public BookingResponseDTO cancelBooking(Long id, String username) {
        Booking booking = getBookingIfHasOwner(id, username);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BusinessValidationException("Нельзя отменить уже выполненный заказ");
        }
        booking.setStatus(BookingStatus.CANCELLED);
        bookingRepository.flush(); // это требуется чтобы заработала аннотация LastModifiedDate в entity над полем updated
        emailService.sendNotificationCancelledByUser(booking.getUser(), booking);
        return bookingMapper.toDTO(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO updateBookingTime(Long id, BookingTimeDTO bookingTimeDTO, String username) {
        Booking booking = getBookingIfHasOwner(id, username);
        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BusinessValidationException("Нельзя вносить изменения в уже выполненный заказ");
        }
        booking.setAppointmentTime(bookingTimeDTO.appointmentTime());
        bookingRepository.flush(); // это требуется чтобы заработала аннотация LastModifiedDate в entity над полем updated
        emailService.sendNotificationUpdateTime(booking.getUser(), booking);
        return bookingMapper.toDTO(booking);
    }

    @Override
    public List<BookingResponseDTO> getAllMyBookings(String username) {
        ApplicationUser neededUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не был найден"));
        return bookingMapper.toDTO(bookingRepository.findAllByUserIdOrderByAppointmentTimeDesc(neededUser.getId()));
    }

    @Override
    public BookingResponseDTO getBookingById(Long id, String username) {
        return bookingMapper.toDTO(getBookingIfHasOwner(id, username));
    }

    @Override
    public List<BookingResponseDTO> getBookingHistory(String username) {
        ApplicationUser neededUser = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь не был найден"));
        return bookingMapper.toDTO(bookingRepository.findAllByUserIdAndStatusOrderByAppointmentTimeDesc(neededUser.getId(), BookingStatus.COMPLETED));
    }

    private Booking getBookingIfHasOwner(Long id, String username) {
        Booking booking = bookingRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Бронь с таким id=" + id + " не существует"));
        if (!booking.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("Доступ запрещен к чужой брони");
        }
        return booking;
    }
}
