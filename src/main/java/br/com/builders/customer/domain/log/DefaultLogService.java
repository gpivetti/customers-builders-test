package br.com.builders.customer.domain.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class DefaultLogService implements LogService {
    private static final Logger LOG = LoggerFactory.getLogger(LogService.class);

    @Override
    public void sendLogError(String token, String errorMessage) {
        this.sendLog(LogData.of(token, errorMessage));
    }

    private void sendLog(LogData logData) {
        LOG.error("at {} - {}: {}", logData.getProcessedAt(), logData.getToken(), logData.getErrorMessage());
    }
}
