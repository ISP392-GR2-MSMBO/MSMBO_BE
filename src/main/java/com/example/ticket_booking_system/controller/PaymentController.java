package com.example.ticket_booking_system.controller;

import com.example.ticket_booking_system.dto.request.payment.CreatePaymentRequest;
import com.example.ticket_booking_system.dto.request.payment.ApiResponse;
import com.example.ticket_booking_system.service.PaymentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.payos.PayOS;
import vn.payos.model.v2.paymentRequests.CreatePaymentLinkResponse;
import vn.payos.model.webhooks.*;



@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
@CrossOrigin(origins = "https://chillcinema.vercel.app/") // cần thêm khi nối React
public class PaymentController {
    private final PayOS payOS;
    private static final Logger logger = LoggerFactory.getLogger(PaymentController.class);
    private final PaymentService paymentService;

    /**
     * ĐÃ CẬP NHẬT: Trả về đối tượng 'CreatePaymentLinkResponse' từ PayOS
     */
    @PostMapping("/create-link")
    public ResponseEntity<?> createPaymentLink(@RequestBody CreatePaymentRequest request) {
        try {
            // 1. Gọi sang Service
            CreatePaymentLinkResponse response = paymentService.createPaymentLink(request);

            // 2. Trả về response thành công từ PayOS (chứa checkoutUrl, ...)
            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponse.error(e.getMessage()));
        } catch (Exception e) {
            logger.error("Error creating payment link: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ApiResponse.error(e.getMessage()));
        }
    }

    @PostMapping(path = "/payos_transfer_handler")
    public ApiResponse<WebhookData> payosTransferHandler(@RequestBody Object request)
            throws JsonProcessingException, IllegalArgumentException {
        try {
            // 1. Xác thực webhook
            WebhookData data = payOS.webhooks().verify(request);
            logger.info("Payment data VERIFIED for orderCode: {}", data.getOrderCode());

            // BƯỚC 2: Gọi Service để xử lý logic
            // Đây chính là "viết code tiếp" mà anh hướng dẫn của bạn nói
            boolean handle = paymentService.handleWebhook(data);

            if (handle) {
                logger.info("Webhook logic PROCESSED successfully for orderCode: {}", data.getOrderCode());
            } else {
                // Đây là trường hợp "Payment not found" (lỗi 123)
                logger.warn("Webhook logic IGNORED (e.g., Payment not found) for orderCode: {}", data.getOrderCode());
            }
            return ApiResponse.success("Webhook processed", data);

        } catch (EntityNotFoundException | IllegalArgumentException e) {
            // 3. Bắt các lỗi nghiệp vụ cụ thể từ Service (ví dụ: không tìm thấy, sai tiền)
            logger.warn("Webhook handling FAILED (Business Logic): {}", e.getMessage());
            return ApiResponse.error(e.getMessage());
        } catch (Exception e) {
            // 4. Bắt các lỗi chung (ví dụ: sai chữ ký, lỗi hệ thống)
            logger.error("Webhook verification or system FAILED: {}", e.getMessage(), e);
            return ApiResponse.error("Webhook processing error: " + e.getMessage());
        }
    }


}
