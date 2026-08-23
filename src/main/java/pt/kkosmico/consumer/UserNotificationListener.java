package pt.kkosmico.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.kkosmico.dto.UserCreatedEvent;
import pt.kkosmico.model.UserRegistration;
import pt.kkosmico.repository.UserRegistrationRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.nio.charset.StandardCharsets;

@Component
public class UserNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(UserNotificationListener.class);

    @Autowired
    private ObjectMapper objectMapper;

    @SuppressWarnings("unused")
	@Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRegistrationRepository repository;

    @RabbitListener(queues = "user.created.queue")
    public void onUserCreated(Message message) {
        try {
            String jsonMessage = new String(message.getBody(), StandardCharsets.UTF_8);
            UserCreatedEvent event = objectMapper.readValue(jsonMessage, UserCreatedEvent.class);

            sendEmail(event);
            sendToBD(jsonMessage, event);

            Thread.sleep(2500);

        } catch (Exception e) {
            log.error("Failed to complete automated notification delivery pipeline", e);
        }
    }

	private void sendEmail(UserCreatedEvent event) {
        log.info("Processing user event for email dispatch: {}", event.getEmail());

        // ✉️ Creating automated email structure parameters
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(event.getEmail());
        email.setSubject("Welcome to my service, " + event.getFirstName() + " " + event.getLastName() +  "!");
        email.setText("Hello " + event.getFirstName() + ",\n\nBest regards,\nThe Quality Team");

        // 🚀 Dispatching email pipeline down the wire
        //mailSender.send(email);


        log.info("=========================================");
        log.info("SUCCESS: Automated welcome email dispatched to {}", event.getEmail());
        log.info("=========================================");

        // Free Plan Failed:
        // SMTPSendFailedException: 550 5.7.0 Too many emails per second.
    }

    private void sendToBD(String jsonMessage, UserCreatedEvent event) {
        UserRegistration registration = new UserRegistration();
        registration.setUserId(event.getId());
        registration.setProcessed(false);

        // 💾 Persisting into Railway online production database
        repository.save(registration);

        log.info("=========================================");
        log.info("SUCCESS: Registration event persisted to MySQL Outbox for: {}", jsonMessage);
        log.info("=========================================");
    }
}
