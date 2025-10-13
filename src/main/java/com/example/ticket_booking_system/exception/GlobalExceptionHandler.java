package com.example.ticket_booking_system.exception;

import com.example.ticket_booking_system.dto.request.movie.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

////    // Bắt tất cả lỗi chưa được xử lý cụ thể
////    @ExceptionHandler(value = Exception.class)
////    public ResponseEntity<ApiResponse> handleGeneralException(Exception exception) {
////        ApiResponse apiResponse = ApiResponse.builder()
////                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
////                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
////                .build();
////        return ResponseEntity.badRequest().body(apiResponse);
////    }
////
    // Bắt lỗi do mình custom bằng AppException
    @ExceptionHandler(value = AppException.class)
    public ResponseEntity<ApiResponse> handleAppException(AppException exception) {
        ErrorCode errorCode = exception.getErrorCode();
        ApiResponse apiResponse = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
////
////    // Bắt lỗi validation (nếu có sử dụng @Valid)
////    @ExceptionHandler(value = MethodArgumentNotValidException.class)
////    public ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException exception) {
////        String enumKey = exception.getFieldError().getDefaultMessage();
////        ErrorCode errorCode = ErrorCode.UNCATEGORIZED_EXCEPTION; // Default error code
////        try {
////            errorCode = ErrorCode.valueOf(enumKey);
////        } catch (IllegalArgumentException e) {
////            // Không khớp thì giữ UNCATEGORIZED_EXCEPTION mặc định
////        }
////        ApiResponse apiResponse = ApiResponse.builder()
////                .code(errorCode.getCode())
////                .message(errorCode.getMessage())
////                .build();
////        return ResponseEntity.badRequest().body(apiResponse);
////    }
@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getFieldErrors()
            .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    Map<String, Object> body = new HashMap<>();
    body.put("code", ErrorCode.UNCATEGORIZED_EXCEPTION.getCode()); // hoặc thêm ErrorCode.VALIDATION nếu có
    body.put("message", "Validation failed");
    body.put("details", errors);

    return ResponseEntity.badRequest().body(body);
}
}
