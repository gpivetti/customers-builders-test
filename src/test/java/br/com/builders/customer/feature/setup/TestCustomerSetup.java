package br.com.builders.customer.feature.setup;

import br.com.builders.customer.infra.mongo.entities.CustomerEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.*;
import org.springframework.stereotype.Component;

@Component
public class TestCustomerSetup {

    private TestRestTemplate restTemplate;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Value("${app.api-user}")
    private String user;

    @Value("${app.api-password}")
    private String password;

    public void startRestTemplate(TestRestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void cleanDatabase() {
        this.mongoTemplate.remove(new Query(), CustomerEntity.class);
    }

    public CustomerEntity getData(String customerId) {
        return this.mongoTemplate.findById(customerId, CustomerEntity.class);
    }

    public long countData() {
        return this.mongoTemplate.count(new Query(), CustomerEntity.class);
    }

    public CustomerEntity insertData(CustomerEntity customerEntity) {
        return this.mongoTemplate.save(customerEntity);
    }

    public <T> ResponseEntity<T> handleCustomersRequest(HttpMethod method, String url, Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(TestCustomerSetup.getDefaultHeaders());
        return this.restTemplate.withBasicAuth(this.user, this.password).exchange(url, method, request, clazz);
    }

    public <T> ResponseEntity<T> handleCustomersRequest(HttpMethod method, String url, Object payload,
                                                        Class<T> clazz) {
        HttpEntity<Object> request = new HttpEntity<>(payload, TestCustomerSetup.getDefaultHeaders());
        return this.restTemplate.withBasicAuth(this.user, this.password).exchange(url, method, request, clazz);
    }

    private static HttpHeaders getDefaultHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.setBasicAuth("builders", "test");
        return httpHeaders;
    }

    public static String makeUrl(int port) {
        return makeUrl(port, null);
    }

    public static String makeUrl(int port, String customerId) {
        return "http://localhost:" + port + "v1/customers" + (customerId != null ? "/" + customerId : "");
    }
}
