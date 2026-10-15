package ru.mikhail.NauJava.dao;

import ru.mikhail.NauJava.entities.FileEntity;
import java.util.List;

public interface FileRepositoryCustom {
    List<FileEntity> findByFileNameAndFileSizeGreaterThan(String fileName, Long fileSize);

    List<FileEntity> findByUploaderUsername(String username);
}