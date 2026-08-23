package pt.kkosmico.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pt.kkosmico.dto.UserCreatedEvent;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

    @Bean
    public MessageConverter jsonMessageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();

        // 1. Create a mapper to intercept the old package name and map it to our new DTO
        DefaultClassMapper classMapper = new DefaultClassMapper();

        // 2. Tell Spring AMQP to accept any incoming type id safely
        classMapper.setTrustedPackages("*");

        // 3. Explicitly remap the old package identity string to your new local class type definition
        Map<String, Class<?>> idClassMapping = new HashMap<>();
        idClassMapping.put("pt.kkosmico.userservice.model.User", UserCreatedEvent.class);
        classMapper.setIdClassMapping(idClassMapping);

        converter.setClassMapper(classMapper);
        return converter;
    }
}
