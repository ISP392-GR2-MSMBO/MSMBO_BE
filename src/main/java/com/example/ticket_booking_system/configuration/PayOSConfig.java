package com.example.ticket_booking_system.configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vn.payos.PayOS;// <-- Import từ SDK mới

@Configuration
public class PayOSConfig {

    @Value("${payos.client-id}")
    private String clientId;

    @Value("${payos.api-key}")
    private String apiKey;

    @Value("${payos.checksum-key}")
    private String checksumKey;

    @Bean // Tạo Bean để inject vào Service
    public PayOS payOS() {
        return new PayOS(clientId, apiKey, checksumKey);
    }
}
