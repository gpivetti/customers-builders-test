package br.com.builders.customer.application.helpers;

import org.springframework.boot.test.web.client.TestRestTemplate;

public class RestTemplateFactory {
    private static final String DEFAULT_USER = "builders";
    private static final String DEFAULT_PASSWORD = "test";

    public static TestRestTemplate get() {
        return new TestRestTemplate(DEFAULT_USER, DEFAULT_PASSWORD);
    }
}
