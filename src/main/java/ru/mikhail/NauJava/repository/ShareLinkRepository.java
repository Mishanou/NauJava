package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mikhail.NauJava.entities.ShareLink;

import java.util.List;

public interface ShareLinkRepository extends CrudRepository<ShareLink, Long> {
    List<ShareLink> findByFileId(Long fileId);
}