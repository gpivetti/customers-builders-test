package br.com.builders.customer.domain.log;

import lombok.Data;

import java.util.Date;

@Data
public class LogData {
    private Date processedAt;
    private String errorMessage;
}
