package br.com.builders.customer.main.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@ApiIgnore
public class HealthCheck {
    private final Logger LOG = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    @ResponseBody
    public ResponseEntity<?> ping() {
        LOG.info("ping api");
        return new ResponseEntity<>(HttpStatus.OK);
    }

}