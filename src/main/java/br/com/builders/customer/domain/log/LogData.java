package br.com.builders.customer.domain.log;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class LogData {
    private LocalDateTime processedAt;
    private String token;
    private String errorMessage;

    private LogData(LocalDateTime processedAt, String token, String errorMessage) {
        this.processedAt = processedAt;
        this.token = token;
        this.errorMessage = errorMessage;
    }

    public static LogData of(String token, String errorMessage) {
        return new LogData(LocalDateTime.now(), token, errorMessage);
    }
}
