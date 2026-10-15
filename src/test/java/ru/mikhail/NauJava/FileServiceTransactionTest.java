package ru.mikhail.NauJava;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.mikhail.NauJava.entities.FileEntity;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.repository.RoleRepository;
import ru.mikhail.NauJava.repository.ShareLinkRepository;
import ru.mikhail.NauJava.repository.UserRepository;
import ru.mikhail.NauJava.service.FileManagementService;
import ru.mikhail.NauJava.util.TestDataFactory;

import java.util.Optional;

@SpringBootTest
class FileServiceTransactionTest {

    @Autowired
    private FileManagementService fileManagementService;

    @Autowired
    private TestDataFactory testDataFactory;

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private ShareLinkRepository shareLinkRepository;

    @Test
    void testDeleteFileWithLinksSuccess() {
        var role = testDataFactory.createRole();
        var user = testDataFactory.createUser(role);
        var file = testDataFactory.createFile(user, "test.txt", 100L);
        var link = testDataFactory.createShareLink(file);

        var fileId = file.getId();
        var linkId = link.getId();

        fileManagementService.deleteFileWithLinks(fileId);

        Assertions.assertTrue(fileRepository.findById(fileId).isEmpty());
        Assertions.assertTrue(shareLinkRepository.findById(linkId).isEmpty());
    }

    @Test
    void testDeleteFileWithLinksRollback() {
        var role = testDataFactory.createRole();
        var user = testDataFactory.createUser(role);
        var safeFile = testDataFactory.createFile(user, "test.txt", 100L);

        var invalidFileId = -9999L;

        try {
            fileManagementService.deleteFileWithLinks(invalidFileId);
        } catch (Exception e) {
            // Игнорируем ожидаемое исключение для проверки отката
        }

        Optional<FileEntity> found = fileRepository.findById(safeFile.getId());
        Assertions.assertTrue(found.isPresent());
    }
}