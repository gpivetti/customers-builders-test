package br.com.builders.customer.domain.log;

public interface LogService {
    void sendLogError(String token, String errorMessage);
}
