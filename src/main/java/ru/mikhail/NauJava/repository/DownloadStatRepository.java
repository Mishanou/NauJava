package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.mikhail.NauJava.entities.DownloadStat;

@RepositoryRestResource
public interface DownloadStatRepository extends CrudRepository<DownloadStat, Long> {
}