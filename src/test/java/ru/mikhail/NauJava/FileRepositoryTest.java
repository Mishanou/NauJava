package ru.mikhail.NauJava;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mikhail.NauJava.entities.File;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.util.TestDataFactory;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@Transactional
class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private TestDataFactory testDataFactory;

    @Test
    void testFindByFileNameAndSize() {
        var role = testDataFactory.createRole();
        var user = testDataFactory.createUser(role);
        var file = testDataFactory.createFile(user, "report.pdf", 2048L);

        List<File> foundFiles = fileRepository.findByFileNameAndFileSizeGreaterThan("report.pdf", 1000L);

        Assertions.assertFalse(foundFiles.isEmpty());
        Assertions.assertEquals(file.getId(), foundFiles.getFirst().getId());
    }

    @Test
    void testFindByUploaderUsername() {
        var uniqueUsername = "uploader_" + UUID.randomUUID().toString();

        var role = testDataFactory.createRole();

        var user = testDataFactory.createUser(role);
        user.setUsername(uniqueUsername);

        var file = testDataFactory.createFile(user, "photo.png", 512L);

        List<File> foundFiles = fileRepository.findByUploaderUsername(uniqueUsername);

        Assertions.assertFalse(foundFiles.isEmpty());
        Assertions.assertEquals(file.getFileName(), foundFiles.getFirst().getFileName());
    }
}