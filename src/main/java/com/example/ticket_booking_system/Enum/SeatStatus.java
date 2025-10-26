package com.example.ticket_booking_system.Enum;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum SeatStatus {
    AVAILABLE,
    UNAVAILABLE,
    EMPTY,
    SOLD;

    @JsonCreator
    public static SeatStatus fromString(String value) {
        if(value == null){
            return null;
        }
        try {
            return SeatStatus.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(value + " is not a valid SeatStatus", e);        }
    }
}
