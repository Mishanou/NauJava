package ru.mikhail.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.mikhail.NauJava.entities.File;
import ru.mikhail.NauJava.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService{

    private final FileRepository fileRepository;

    @Autowired
    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    @Override
    public void uploadFile(Long id, String name, Long size) {
        var file = new File(id, name, size);
        file.setDownloadURL("http://share.local/file/" + id);
        fileRepository.create(file);
    }

    @Override
    public File findById(Long id) {
        return fileRepository.read(id);
    }

    @Override
    public File downloadByUrl(String downloadUrl) {
        return fileRepository.readAll().stream()
                .filter(file -> downloadUrl.equals(file.getDownloadURL()))
                .findFirst()
                .orElse(null);
    }

    @Override
    public boolean deleteById(Long id) {
        return fileRepository.delete(id);
    }
}
