package com.example.ticket_booking_system.service;

import com.example.ticket_booking_system.dto.reponse.booking.BookingResponse;
import com.example.ticket_booking_system.entity.Booking;
import com.example.ticket_booking_system.entity.Payment;
import com.example.ticket_booking_system.entity.User;
import com.example.ticket_booking_system.mapper.BookingMapper;
import com.example.ticket_booking_system.repository.BookingRepository;
import com.example.ticket_booking_system.repository.PaymentRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${spring.mail.username}") String from;
    private final JavaMailSender mailSender;

    // === BƯỚC MỚI: Inject TemplateEngine (để build HTML) ===
    private final SpringTemplateEngine templateEngine;

    private final BookingRepository bookingRepository;
    private final PaymentRepository paymentRepository;

    /**
     * Gửi email xác thực (Bất đồng bộ)
     */
    @Async // Thêm @Async để chạy nền
    public void sendEmailVerification(String to, String verifyLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from, "Movie Booking"); // hoặc dùng InternetAddress cũng được
            helper.setTo(to);
            helper.setSubject("Verify your email");
            helper.setText(
                    "<p>Nhấn vào link để xác thực:</p><p><a href=\"" + verifyLink + "\">" + verifyLink + "</a></p>",
                    true
            );
            mailSender.send(mimeMessage);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            // log lỗi, tránh làm nổ API
            log.error("Send verification email failed", e);
        }
    }

    @Async
    // (Hàm cũ của bạn, giữ nguyên)
    public void sendPasswordResetEmail(String to, String resetLink) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from, "Movie Booking");
            helper.setTo(to);
            helper.setSubject("Reset Your Password"); // Tiêu đề mới
            helper.setText(
                    "<p>Nhấn vào link để đặt lại mật khẩu:</p><p><a href=\"" + resetLink + "\">" + resetLink + "</a></p>" +
                            "<p>Link này sẽ hết hạn sau 20 phút.</p>",
                    true
            );
            mailSender.send(mimeMessage);
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Send password reset email failed", e);
        }
    }

    /**
     * HÀM MỚI: Gửi vé điện tử
     * Bật @Transactional(readOnly = true) để đảm bảo các trường LAZY-LOADING
     * (như user, showtime, movie, bookingDetails...) được tải lên khi truy cập
     */
    @Async
    @Transactional(readOnly = true)
    public void sendElectronicTicket(Long bookingId) {
        log.info("Attempting to send e-ticket for bookingId: {}", bookingId);

        // 3.1. Tải (fetch) booking MỘT CÁCH TƯƠI MỚI bên trong transaction này
        Booking booking = bookingRepository.findById(bookingId)
                .orElse(null); // Không tìm thấy thì trả về null

        if (booking == null) {
            log.error("Failed to send e-ticket: Booking with id {} not found.", bookingId);
            return; // Dừng lại nếu không tìm thấy booking
        }

        // 3.2. Lấy thông tin người nhận (Bây giờ đã an toàn để lazy-load)
        User user = booking.getUser();
        String toEmail = user.getEmail();
        String toName = user.getFullName();

        // 3.3. Lấy thông tin vé (Bây giờ đã an toàn để lazy-load)
        BookingResponse ticketData = BookingMapper.toBookingResponse(booking);

        // 3.4. Tạo nội dung email HTML
        String subject = "Vé xem phim của bạn tại ChillCinema - Mã vé #" + ticketData.getBookingID();
        String htmlBody = buildTicketHtml(ticketData, toName);

        // 3.5. Gửi email
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from, "ChillCinema");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(mimeMessage);

            log.info("E-ticket sent successfully to {} for bookingId: {}", toEmail, bookingId);

        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            log.error("Send e-ticket email failed for bookingId: {}", booking.getBookingID(), e);
            // Không ném lỗi ra ngoài vì đây là hàm @Async
        }
    }

    // Hàm phụ tiện ích để tạo HTML (bạn có thể thay bằng Thymeleaf nếu muốn)
    private String buildTicketHtml(BookingResponse ticket, String userName) {
        // Định dạng ngày giờ
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        // Lấy danh sách ghế
        String seatsHtml = ticket.getSeats().stream()
                .map(seat -> String.format("<li>Ghế: %s%d (%.0f VNĐ)</li>",
                        seat.getSeatRow(),
                        seat.getSeatNumber(),
                        seat.getPrice()))
                .collect(Collectors.joining(""));

        // Tạo template HTML
        return String.format("""
            <div style="font-family: Arial, sans-serif; line-height: 1.6; color: #333;">
                <h2 style="color: #000;">Chào %s,</h2>
                <p>Cảm ơn bạn đã đặt vé tại <strong>ChillCinema</strong>. Vé của bạn đã được xác nhận thành công!</p>
                
                <h3 style="color: #d9534f; border-bottom: 2px solid #d9534f; padding-bottom: 5px;">Chi tiết vé điện tử</h3>
                <div style="border: 1px solid #ddd; padding: 15px; border-radius: 5px; background-color: #f9f9f9;">
                    <p><strong>Mã vé:</strong> <span style="font-size: 1.1em; color: #d9534f;">#%d</span></p>
                    <p><strong>Phim:</strong> %s</p>
                    <p><strong>Rạp:</strong> %s</p>
                    <p><strong>Ngày chiếu:</strong> %s</p>
                    <p><strong>Giờ chiếu:</strong> %s</p>
                    
                    <h4>Chi tiết ghế:</h4>
                    <ul style="list-style-type: none; padding-left: 0;">
                        %s
                    </ul>
                    
                    <hr>
                    <p style="font-size: 1.2em; font-weight: bold; text-align: right;">
                        Tổng cộng: %.0f VNĐ
                    </p>
                </div>
                
                <p style="margin-top: 20px;">Vui lòng đưa email này (hoặc mã vé) cho nhân viên tại quầy để nhận vé.</p>
                <p>Chúc bạn xem phim vui vẻ!</p>
                <p style="margin-top: 25px; color: #777;">Trân trọng,<br>Đội ngũ ChillCinema</p>
            </div>
            """,
                userName,
                ticket.getBookingID(),
                ticket.getMovieName(),
                ticket.getTheaterName(),
                ticket.getShowDate().format(dateFormatter),
                ticket.getStartTime().format(timeFormatter),
                seatsHtml,
                ticket.getTotalPrice()
        );
    }
}

