package pt.kkosmico.notificationservice.repository;

import pt.kkosmico.notificationservice.model.UserRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {
    // This will allow saving data directly to the MySQL DB
}
