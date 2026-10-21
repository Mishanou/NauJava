package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.mikhail.NauJava.entities.User;

@RepositoryRestResource
public interface UserRepository extends CrudRepository<User, Long> {
}
