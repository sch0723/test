package com.cy.project.config;

import com.cy.project.listener.RedisExpirationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        StringRedisSerializer stringRedisSerializer=new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer=new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(genericJackson2JsonRedisSerializer);
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(genericJackson2JsonRedisSerializer);

        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory redisConnectionFactory) {

        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();

        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);

        return redisMessageListenerContainer;
    }

    @Bean
    public RedisExpirationListener redisExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer){

        return new RedisExpirationListener(redisMessageListenerContainer);

    }
}
