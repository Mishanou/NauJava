package ru.mikhail.NauJava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.TransactionDefinition;
import ru.mikhail.NauJava.entities.ShareLink;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.repository.ShareLinkRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileManagementServiceImplTest {

    private static final Long FILE_ID = 1L;

    @Mock
    private FileRepository fileRepository;

    @Mock
    private ShareLinkRepository shareLinkRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @Mock
    private TransactionStatus transactionStatus;

    @Mock
    private ShareLink link1;

    @Mock
    private ShareLink link2;

    @InjectMocks
    private FileManagementServiceImpl fileManagementService;

    @BeforeEach
    void setUp() {
        when(transactionManager.getTransaction(any(TransactionDefinition.class)))
                .thenReturn(transactionStatus);
    }

    // 1. Успешное удаление файла вместе со ссылками
    @Test
    void deleteFileWithLinks_success() {
        when(shareLinkRepository.findByFileId(FILE_ID))
                .thenReturn(List.of(link1, link2));

        fileManagementService.deleteFileWithLinks(FILE_ID);

        verify(shareLinkRepository).delete(link1);
        verify(shareLinkRepository).delete(link2);
        verify(fileRepository).deleteById(FILE_ID);

        verify(transactionManager).commit(transactionStatus);
        verify(transactionManager, never()).rollback(any());
    }

    // 2. Успешное удаление файла без ссылок
    @Test
    void deleteFileWithLinks_noLinks() {
        when(shareLinkRepository.findByFileId(FILE_ID))
                .thenReturn(List.of());

        fileManagementService.deleteFileWithLinks(FILE_ID);

        verify(shareLinkRepository, never()).delete(any(ShareLink.class));
        verify(fileRepository).deleteById(FILE_ID);

        verify(transactionManager).commit(transactionStatus);
        verify(transactionManager, never()).rollback(any());
    }

    // 3. Ошибка при получении ссылок
    @Test
    void deleteFileWithLinks_findLinksThrowsException() {
        when(shareLinkRepository.findByFileId(FILE_ID))
                .thenThrow(new DataIntegrityViolationException("Database error"));

        assertThrows(
                DataIntegrityViolationException.class,
                () -> fileManagementService.deleteFileWithLinks(FILE_ID)
        );

        verify(fileRepository, never()).deleteById(any());
        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(any());
    }

    // 4. Ошибка при удалении ссылки
    @Test
    void deleteFileWithLinks_deleteLinkThrowsException() {
        when(shareLinkRepository.findByFileId(FILE_ID))
                .thenReturn(List.of(link1));

        doThrow(new DataIntegrityViolationException("Database error"))
                .when(shareLinkRepository).delete(link1);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> fileManagementService.deleteFileWithLinks(FILE_ID)
        );

        verify(fileRepository, never()).deleteById(any());
        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(any());
    }

    // 5. Ошибка при удалении файла
    @Test
    void deleteFileWithLinks_deleteFileThrowsException() {
        when(shareLinkRepository.findByFileId(FILE_ID))
                .thenReturn(List.of(link1));

        doThrow(new DataIntegrityViolationException("Database error"))
                .when(fileRepository).deleteById(FILE_ID);

        assertThrows(
                DataIntegrityViolationException.class,
                () -> fileManagementService.deleteFileWithLinks(FILE_ID)
        );

        verify(shareLinkRepository).delete(link1);
        verify(transactionManager).rollback(transactionStatus);
        verify(transactionManager, never()).commit(any());
    }
}
