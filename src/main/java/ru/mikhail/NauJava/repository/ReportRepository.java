package ru.mikhail.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.mikhail.NauJava.entities.Report;

public interface ReportRepository extends CrudRepository<Report, Long> {
}
