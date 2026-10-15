package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mikhail.NauJava.entities.DownloadStat;

public interface DownloadStatRepository extends CrudRepository<DownloadStat, Long> {
}