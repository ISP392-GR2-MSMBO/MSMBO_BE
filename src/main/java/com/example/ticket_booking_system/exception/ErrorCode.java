package com.example.ticket_booking_system.exception;

import org.aspectj.apache.bcel.classfile.Code;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    MOVIE_EXISTED(1000, "Movie is existed!"),
    MOVIE_NOT_FOUND(1001, "Movie is not found!"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_EXISTED(1003, "Username existed"),
    INVALID_PASSWORD(1004, "Password must be at least 8 characters"),
    SHOWTIME_NOT_FOUND(1005, "Showtime not found"),
    MISSING_MOVIE_ID(1006, "Missing movie ID to update"),
    INVALID_STATUS(1007, "Invalid movie status (Now showing||Coming soon||Ended)"),
    THEATER_NOT_FOUND(1008, "Theater has not been found!"),
    SHOWTIME_CONFLICT(1009, "Showtime is existed!"),
    EMAIL_EXISTED(1010, "Email already exists"),
    PHONE_EXISTED(1011, "Phone already exists"),
    USER_NOT_FOUND(1012, "User not found"),
    CUSTOMER_NOT_FOUND(1013, "Customer not found"),
    MOVIE_ALREADY_DELETED(1014, "Movie has already been deleted"),
    MOVIE_DELETED_OR_INACTIVE(1015, "Movie is deleted or inactive"),
    SEAT_NOT_FOUND(1016, "Seat not found"),
    SEAT_UNAVAILABLE_DUE_TO_DAMAGE(1017, "Cannot update seat to SOLD or EMPTY due to broken seat"),
    INVALID_SEAT_STATUS_TRANSITION(1018, "Cannot transfer sold seat to empty seat"),
    INVALID_DATE_RANGE(1019, "Date is not in range"),
    PROMOTION_NOT_FOUND(1020, "Promotion not found");{
    }

    private final int code;
    private final String message;

    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
