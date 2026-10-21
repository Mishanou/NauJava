package ru.mikhail.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.mikhail.NauJava.entities.File;

import java.util.List;

@RepositoryRestResource
public interface FileRepository extends CrudRepository<File, Long> {
    List<File> findByFileNameAndFileSizeGreaterThan(String fileName, Long fileSize);

    @Query("SELECT f FROM FileEntity f WHERE f.uploader.username = :username")
    List<File> findByUploaderUsername(@Param("username") String username);
}