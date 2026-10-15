package ru.mikhail.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.mikhail.NauJava.entities.ShareLink;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.repository.ShareLinkRepository;

import java.util.List;

@Service
public class FileManagementServiceImpl implements FileManagementService {

    private final FileRepository fileRepository;
    private final ShareLinkRepository shareLinkRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public FileManagementServiceImpl(FileRepository fileRepository,
                                     ShareLinkRepository shareLinkRepository,
                                     PlatformTransactionManager transactionManager) {
        this.fileRepository = fileRepository;
        this.shareLinkRepository = shareLinkRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    public void deleteFileWithLinks(Long fileId) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());

        try {
            List<ShareLink> links = shareLinkRepository.findByFileId(fileId);
            for (var link : links)
                shareLinkRepository.delete(link);

            fileRepository.deleteById(fileId);

            transactionManager.commit(status);
        } catch (DataAccessException ex) {
            transactionManager.rollback(status);
            throw ex;
        }
    }
}