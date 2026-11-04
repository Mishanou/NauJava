package ru.mikhail.NauJava.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import ru.mikhail.NauJava.entities.Report;
import ru.mikhail.NauJava.entities.ReportStatus;
import ru.mikhail.NauJava.service.ReportService;

import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, Object>> generateReport() {

        Long reportId = reportService.createReport();

        reportService.generateReportAsync(reportId);

        return ResponseEntity
                .status(HttpStatus.ACCEPTED)
                .body(Map.of(
                        "reportId", reportId,
                        "status", ReportStatus.CREATED,
                        "message", "Формирование отчёта запущено"
                ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getReport(@PathVariable Long id) {

        return reportService.getReport(id)
                .map(this::buildReportResponse)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "message", "Отчёт с указанным ID не найден"
                        )));
    }

    private ResponseEntity<?> buildReportResponse(Report report) {

        if (report.getStatus() == ReportStatus.CREATED) {

            return ResponseEntity
                    .status(HttpStatus.ACCEPTED)
                    .body(Map.of(
                            "reportId", report.getId(),
                            "status", report.getStatus(),
                            "message", "Отчёт ещё формируется"
                    ));
        }

        if (report.getStatus() == ReportStatus.ERROR) {

            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "reportId", report.getId(),
                            "status", report.getStatus(),
                            "message", "При формировании отчёта произошла ошибка"
                    ));
        }

        return ResponseEntity
                .ok()
                .contentType(MediaType.TEXT_HTML)
                .body(report.getContent());
    }
}