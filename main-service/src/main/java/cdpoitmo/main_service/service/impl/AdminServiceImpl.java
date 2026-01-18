package cdpoitmo.main_service.service.impl;

import cdpoitmo.main_service.constants.BookingStatus;
import cdpoitmo.main_service.constants.UserRole;
import cdpoitmo.main_service.dto.adminDTO.CreateOperatorRequestDTO;
import cdpoitmo.main_service.dto.adminDTO.DiscountRequestDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingResponseDTO;
import cdpoitmo.main_service.dto.bookingDTO.BookingTimeDTO;
import cdpoitmo.main_service.dto.bookingDTO.RevenueDTO;
import cdpoitmo.main_service.entity.ApplicationUser;
import cdpoitmo.main_service.entity.Booking;
import cdpoitmo.main_service.exception.BusinessValidationException;
import cdpoitmo.main_service.exception.ResourceNotFoundException;
import cdpoitmo.main_service.exception.UserAlreadyExistsException;
import cdpoitmo.main_service.mapper.BookingMapper;
import cdpoitmo.main_service.repository.BookingRepository;
import cdpoitmo.main_service.repository.UserRepository;
import cdpoitmo.main_service.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminServiceImpl implements AdminService {

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    @Override
    public List<BookingResponseDTO> getBookingsInPeriod(
            LocalDateTime start,
            LocalDateTime end) {
        return bookingMapper.toDTO(bookingRepository.findAllByAppointmentTimeBetween(start, end));
    }

    @Override
    public List<RevenueDTO> getRevenueInPeriod(LocalDate from, LocalDate to) {
        LocalDateTime start = from.atStartOfDay();
        LocalDateTime end = to.atTime(23, 59, 59);

        return bookingRepository.getStatisticReport(start, end);
    }

    @Transactional
    @Override
    public String createOperator(CreateOperatorRequestDTO createOperatorRequestDTO) {
        if (userRepository.findByUsername(createOperatorRequestDTO.username()).isPresent()) {
            throw new UserAlreadyExistsException("Пользователь с таким именем уже существует");
        }

        ApplicationUser operator = ApplicationUser.builder()
                .username(createOperatorRequestDTO.username())
                .password(passwordEncoder.encode(createOperatorRequestDTO.password()))
                .email(createOperatorRequestDTO.email())
                .userRole(UserRole.OPERATOR)
                .build();

        userRepository.save(operator);

        return "Оператор был создан успешно";
    }

    @Transactional
    @Override
    public BookingResponseDTO timeUpdateBooking(Long id, BookingTimeDTO requestDTO) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Заказа с таким id не существует")
        );
        booking.setAppointmentTime(requestDTO.appointmentTime());
        bookingRepository.flush();
        return bookingMapper.toDTO(booking);
    }

    @Transactional
    @Override
    public BookingResponseDTO setDiscountBooking(Long id, DiscountRequestDTO request) {
        Booking booking = bookingRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Заказа с таким id не существует")
        );

        double discount = request.discount();
        booking.setDiscountPercent(discount);

        BigDecimal originalPrice = booking.getTechServiceEntity().getPrice();
        BigDecimal discountCoef = BigDecimal.valueOf(100 - discount).divide(BigDecimal.valueOf(100));
        BigDecimal newPrice = originalPrice.multiply(discountCoef).setScale(2, RoundingMode.HALF_UP);

        booking.setFinalPrice(newPrice);
        bookingRepository.flush();
        return bookingMapper.toDTO(booking);
    }

    @Override
    @Transactional
    public BookingResponseDTO cancelBookingByAdmin(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(
                () -> new ResourceNotFoundException("Бронь с таким id не существует")
        );

        if (booking.getStatus() == BookingStatus.COMPLETED) {
            throw new BusinessValidationException("Нельзя отменить выполненную услугу");
        }

        booking.setStatus(BookingStatus.CANCELLED);

        ApplicationUser user = booking.getUser();
        user.setPersonalDiscount(15.0);

        bookingRepository.flush();
        emailService.sendNotificationCancelledByAdmin(booking.getUser(), booking);

        return bookingMapper.toDTO(booking);
    }
}
