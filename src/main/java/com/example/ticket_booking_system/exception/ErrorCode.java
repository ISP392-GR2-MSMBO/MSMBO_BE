package com.example.ticket_booking_system.exception;

import org.aspectj.apache.bcel.classfile.Code;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error"),
    MOVIE_EXISTED(1000, "Movie is existed!"),
    MOVIE_NOT_FOUND(1001, "Movie is not found!"),
    USER_EXISTED(1002, "User existed"),
    USERNAME_EXISTED(1003, "Username existed"),
    INVALID_PASSWORD(1004, "Wrong password"),
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
    SEAT_UNAVAILABLE_DUE_TO_DAMAGE(1017, "Cannot update seat to SOLD due to broken seat"),
    INVALID_SEAT_STATUS_TRANSITION(1018, "Cannot transfer sold seat to empty seat"),
    CANNOT_EDIT_PUBLISHED(1019, "Published showtime cannot be edited"),
    SHOWTIME_ALREADY_PUBLISHED(1020, "Showtime already published"),
    PAST_SHOWTIME_CANNOT_PUBLISH(1021, "Past showtime can not publish"),
    MOVIE_NOT_APPROVED(1022, "Movie is not approved"),
    INVALID_DATE_RANGE(1023, "Date is not in range"),
    PROMOTION_NOT_FOUND(1024, "Promotion not found"),
    SHOWTIME_ALREADY_DELETED(1025, "Showtime has already been deleted"),
    PROMOTION_NAME_EXISTS(1026, "Promotion name has exists"),
    INVALID_TOKEN(1027,"Token invalid!"),
    TOKEN_EXPIRED(1028, "Token expired!"),
    SEAT_TYPE_NOT_FOUND(1029, "Seat type not found"),
    SEAT_ALREADY_EXISTS(1030, "Seat already exists, Can not add"),
    SEAT_ALREADY_BOOKED(1031, "seat already booked!"),
    COMBO_NOT_FOUND(1032, "Combo not found!"),
    BOOKING_NOT_FOUND(1033, "Booking not found!"),
    BOOKING_DETAIL_NOT_FOUND(1034, "Booking detail (seat) not found!"),
    REPORT_NOT_FOUND(1035, "Report not found!");
    {
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
