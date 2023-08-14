package knl.msvc.elevator.system.Internal.call.configurations;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


/**
 * @Autor janielhernandez
 * @date  08/08/2023
 */
@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {
        final RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory( factory );
        template.setKeySerializer( new StringRedisSerializer() );
        template.setHashValueSerializer(new GenericToStringSerializer<>(String.class));
        template.setValueSerializer(new GenericToStringSerializer<>(String.class));
        return template;
    }
}
