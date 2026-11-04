package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.mikhail.NauJava.entities.Role;

import java.util.Optional;

@RepositoryRestResource
public interface RoleRepository extends CrudRepository<Role, Long> {
    Optional<Role> findByCode(String code);
}
