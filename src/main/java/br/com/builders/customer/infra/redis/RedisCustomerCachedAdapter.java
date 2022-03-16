package br.com.builders.customer.infra.redis;

import br.com.builders.customer.controllers.customer.dto.CustomerDTO;
import br.com.builders.customer.domain.customer.adapters.CustomerCacheAdapter;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class RedisCustomerCachedAdapter implements CustomerCacheAdapter {

    private final Gson gsonTemplate;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public RedisCustomerCachedAdapter(final RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.gsonTemplate = new Gson();
    }

    @Override
    public CustomerDTO findById(String customerId) {
        String jsonResponse = this.redisTemplate.opsForValue().get(customerId);
        return gsonTemplate.fromJson(jsonResponse, CustomerDTO.class);
    }

    @Override
    public void save(CustomerDTO customerDTO) {
        String jsonResponse = gsonTemplate.toJson(customerDTO, CustomerDTO.class);
        redisTemplate.opsForValue().set(customerDTO.getId(), jsonResponse);
        redisTemplate.expire(customerDTO.getId(), Duration.ofMinutes(1));
    }
}
