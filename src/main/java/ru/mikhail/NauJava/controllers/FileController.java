package ru.mikhail.NauJava.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.mikhail.NauJava.entities.File;
import ru.mikhail.NauJava.repository.FileRepository;

import java.util.List;

@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileRepository fileRepository;

    @GetMapping("/findByNameAndSize")
    public List<File> findByNameAndSize(@RequestParam String name, Long size) {
        return fileRepository.findByFileNameAndFileSizeGreaterThan(name, size);
    }

    @GetMapping("/findByUploader")
    public List<File> findByUploaderUsername(@RequestParam String username) {
        return fileRepository.findByUploaderUsername(username);
    }

}
