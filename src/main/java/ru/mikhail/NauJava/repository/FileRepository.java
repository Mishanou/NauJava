package ru.mikhail.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.mikhail.NauJava.entities.FileEntity;

import java.util.List;

public interface FileRepository extends CrudRepository<FileEntity, Long> {
    List<FileEntity> findByFileNameAndFileSizeGreaterThan(String fileName, Long fileSize);

    @Query("SELECT f FROM FileEntity f WHERE f.uploader.username = :username")
    List<FileEntity> findByUploaderUsername(@Param("username") String username);
}