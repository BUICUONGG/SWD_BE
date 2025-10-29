package swd.fpt.exegroupingmanagement.service.impl;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import swd.fpt.exegroupingmanagement.service.RedisService;

@Slf4j
@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void save(String key, String value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }

    @Override
    public void save(String key, String value, long duration, TimeUnit timeUnit) {
        try {
            redisTemplate.opsForValue().set(key, value, duration, timeUnit);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }

    @Override
    public String get(String key) {
        try {
            return (String) redisTemplate.opsForValue().get(key);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }

    @Override
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }

    @Override
    public boolean exists(String key) {
        try {
            return redisTemplate.hasKey(key);
        } catch (Exception e) {
            log.error("Unable to connect to Redis: {}", e.getMessage());
            throw new RuntimeException("Unable to connect to Redis", e);
        }
    }
}