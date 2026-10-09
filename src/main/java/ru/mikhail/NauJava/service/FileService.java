package ru.mikhail.NauJava.service;

import ru.mikhail.NauJava.entities.File;

public interface FileService {
    void uploadFile(Long id, String name, Long size);

    File findById(Long id);

    File downloadByUrl(String downloadUrl);

    boolean deleteById(Long id);
}