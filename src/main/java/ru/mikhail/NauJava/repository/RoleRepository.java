package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mikhail.NauJava.entities.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}