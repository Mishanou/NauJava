package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mikhail.NauJava.entities.User;

public interface UserRepository extends CrudRepository<User, Long> {
}