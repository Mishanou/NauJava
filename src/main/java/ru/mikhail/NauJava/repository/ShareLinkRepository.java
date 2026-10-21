package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.mikhail.NauJava.entities.ShareLink;

import java.util.List;

@RepositoryRestResource
public interface ShareLinkRepository extends CrudRepository<ShareLink, Long> {
    List<ShareLink> findByFileId(Long fileId);
}