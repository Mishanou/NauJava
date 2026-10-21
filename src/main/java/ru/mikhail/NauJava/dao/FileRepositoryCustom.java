package ru.mikhail.NauJava.dao;

import ru.mikhail.NauJava.entities.File;
import java.util.List;

public interface FileRepositoryCustom {
    List<File> findByFileNameAndFileSizeGreaterThan(String fileName, Long fileSize);

    List<File> findByUploaderUsername(String username);
}