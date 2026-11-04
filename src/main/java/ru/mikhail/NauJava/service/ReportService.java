package ru.mikhail.NauJava.service;

import ru.mikhail.NauJava.entities.Report;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ReportService {
    Long createReport();
    Optional<Report> getReport(Long id);
    CompletableFuture<Void> generateReportAsync(Long reportId);
}
