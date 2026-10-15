package ru.mikhail.NauJava.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.mikhail.NauJava.entities.File;

@Component
public class FileRepository implements CrudRepository<File, Long> {

    private final List<File> fileContainer;

    @Autowired
    public FileRepository(List<File> fileContainer) {
        this.fileContainer = fileContainer;
    }

    @Override
    public void create(File file) {
        fileContainer.add(file);
    }

    @Override
    public File read(Long id) {
        return fileContainer.stream()
                .filter(file -> file.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void update(File newFile) {
        var existingFile = read(newFile.getId());
        if (existingFile != null) {
            existingFile.setName(newFile.getName());
            existingFile.setDownloadURL(newFile.getDownloadURL());
            existingFile.setSize(newFile.getSize());
        }
    }

    @Override
    public boolean delete(Long id) {
        return fileContainer.removeIf(file -> file.getId() == id);
    }

    public List<File> readAll() {
        return fileContainer;
    }
}