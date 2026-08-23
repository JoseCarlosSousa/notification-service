package pt.kkosmico.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import pt.kkosmico.model.UserRegistration;

@Repository
public interface UserRegistrationRepository extends JpaRepository<UserRegistration, Long> {
    // This will allow saving data directly to the MySQL DB
}
