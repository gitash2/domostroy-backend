package domostroy.auth.users.service;


import domostroy.auth.dto.VerificationData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final RedisTemplate<String, VerificationData> redisTemplate;


    public void saveVerificationCode(String userId, VerificationData data, long ttl, TimeUnit unit) {
        String key = getKey(userId);
        redisTemplate.opsForValue().set(key, data , ttl, unit);
    }

    public Optional<VerificationData> getVerificationData(String email) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(getKey(email)));
    }

    public void deleteVerificationCode(String userId) {
        String key = getKey(userId);
        redisTemplate.delete(key);
    }

    private String getKey(String userId) {
        return "verification:code:" + userId;
    }
}
