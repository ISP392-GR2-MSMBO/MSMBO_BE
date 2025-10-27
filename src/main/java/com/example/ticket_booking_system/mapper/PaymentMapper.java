package com.example.ticket_booking_system.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import com.example.ticket_booking_system.entity.Payment;
import com.example.ticket_booking_system.dto.reponse.payment.PaymentResponseDTO;

@Mapper(componentModel = "spring")
public interface PaymentMapper {

    PaymentMapper INSTANCE = Mappers.getMapper(PaymentMapper.class);

    @Mapping(source = "booking.bookingID", target = "bookingID")
    @Mapping(source = "status", target = "status")
    PaymentResponseDTO toPaymentResponseDTO(Payment payment);
}