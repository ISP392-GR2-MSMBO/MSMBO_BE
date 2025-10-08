package com.example.ticket_booking_system.exception;

//@ControllerAdvice
//public class GlobalExceptionHandler {
//    // Bắt tất cả lỗi chưa được xử lý cụ thể
//    @ExceptionHandler(value = Exception.class)
//    ResponseEntity<ApiResponse> handleGeneralException(RuntimeException exception) {
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode());
//        apiResponse.setMessage(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//    // Bắt lỗi do mình custom bằng AppException
//    @ExceptionHandler(value = AppException.class)
//    ResponseEntity<ApiResponse> handleAppException(AppException exception) {
//        ErrorCode errorCode = exception.getErrorCode();
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }
//
//    // Bắt lỗi validation (nếu có sử dụng @Valid)
//    /*@ExceptionHandler(value = MethodArgumentNotValidException.class)
//    ResponseEntity<ApiResponse> handleValidation(MethodArgumentNotValidException exception) {
//        String enumKey = exception.getFieldError().getDefaultMessage();
//        ErrorCode errorCode = ErrorCode.INVALID_KEY;
//        try {
//            errorCode = ErrorCode.valueOf(enumKey);
//        } catch (IllegalArgumentException e) {
//            // Không khớp thì giữ INVALID_KEY mặc định
//        }
//        ApiResponse apiResponse = new ApiResponse();
//        apiResponse.setCode(errorCode.getCode());
//        apiResponse.setMessage(errorCode.getMessage());
//        return ResponseEntity.badRequest().body(apiResponse);
//    }*/
//}
