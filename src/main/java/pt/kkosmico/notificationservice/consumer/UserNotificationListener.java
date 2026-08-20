package pt.kkosmico.notificationservice.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import pt.kkosmico.notificationservice.dto.UserCreatedEvent;

import java.nio.charset.StandardCharsets;

@Component
public class UserNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JavaMailSender mailSender;

    @RabbitListener(queues = "user.created.queue")
    public void onUserCreated(Message message) {
        try {
            String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            UserCreatedEvent event = objectMapper.readValue(jsonMessage, UserCreatedEvent.class);

            log.info("Processing user event for email dispatch: {}", event.getEmail());

            // ✉️ Creating automated email structure parameters
            SimpleMailMessage email = new SimpleMailMessage();
            email.setTo(event.getEmail());
            email.setSubject("Welcome to my service, " + event.getName() + "!");
            email.setText("Hello " + event.getName() + ",\n\nBest regards,\nThe Quality Team");

            // 🚀 Dispatching email pipeline down the wire
            mailSender.send(email);


            log.info("=========================================");
            log.info("SUCCESS: Automated welcome email dispatched to {}", event.getEmail());
            log.info("=========================================");

            // Free Plan Failed:
            // SMTPSendFailedException: 550 5.7.0 Too many emails per second.
            Thread.sleep(2500);


        } catch (Exception e) {
            log.error("Failed to complete automated notification delivery pipeline", e);
        }
    }
}
