package com.example.ticket_booking_system.dto.request.movie;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppResponse<T> {
    @Builder.Default
    private int code = 1000;
    private String message;
    private T result;
}
