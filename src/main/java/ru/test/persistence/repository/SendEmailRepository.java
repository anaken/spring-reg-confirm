package ru.test.persistence.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.test.persistence.entity.SendEmail;

import java.util.List;

public interface SendEmailRepository extends JpaRepository<SendEmail, String> {

    @Query("select e from SendEmail e where e.status = 'CREATED'")
    List<SendEmail> findNotSent(Pageable pageable);
}
