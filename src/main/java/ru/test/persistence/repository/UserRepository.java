package ru.test.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.test.persistence.entity.User;

public interface UserRepository extends JpaRepository<User, String> {
}
