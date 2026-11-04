package ru.mikhail.NauJava.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import ru.mikhail.NauJava.entities.File;
import ru.mikhail.NauJava.entities.Report;
import ru.mikhail.NauJava.entities.ReportStatus;
import ru.mikhail.NauJava.repository.FileRepository;
import ru.mikhail.NauJava.repository.ReportRepository;
import ru.mikhail.NauJava.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class ReportServiceImpl implements ReportService {

    private final ReportRepository reportRepository;
    private final UserRepository userRepository;
    private final FileRepository fileRepository;

    public ReportServiceImpl(
            ReportRepository reportRepository,
            UserRepository userRepository,
            FileRepository fileRepository
    ) {
        this.reportRepository = reportRepository;
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
    }

    @Override
    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);

        return reportRepository.save(report).getId();
    }

    @Override
    public Optional<Report> getReport(Long id) {
        return reportRepository.findById(id);
    }

    @Override
    @Async
    public CompletableFuture<Void> generateReportAsync(Long reportId) {

        Optional<Report> optionalReport = reportRepository.findById(reportId);

        if (optionalReport.isEmpty()) {
            return CompletableFuture.completedFuture(null);
        }

        var report = optionalReport.get();

        long totalStartTime = System.currentTimeMillis();

        var usersCount = new AtomicLong(0);
        var usersTime = new AtomicLong(0);

        AtomicReference<List<FileReportItem>> files =
                new AtomicReference<>(new ArrayList<>());

        var filesTime = new AtomicLong(0);

        AtomicReference<Exception> error = new AtomicReference<>();

        try {
            // Первый поток: подсчёт пользователей
            var usersThread = new Thread(() -> {
                long startTime = System.currentTimeMillis();

                try {
                    usersCount.set(userRepository.count());
                } catch (Exception e) {
                    error.compareAndSet(null, e);
                } finally {
                    usersTime.set(
                            System.currentTimeMillis() - startTime
                    );
                }
            });

            // Второй поток: получение списка файлов
            var filesThread = new Thread(() -> {
                long startTime = System.currentTimeMillis();

                try {
                    List<FileReportItem> fileItems = new ArrayList<>();

                    Iterable<File> foundFiles = fileRepository.findAll();

                    for (var file : foundFiles) {
                        fileItems.add(new FileReportItem(
                                file.getId(),
                                file.getFileName(),
                                file.getContentType(),
                                file.getFileSize()
                        ));
                    }

                    files.set(fileItems);

                } catch (Exception e) {
                    error.compareAndSet(null, e);
                } finally {
                    filesTime.set(
                            System.currentTimeMillis() - startTime
                    );
                }
            });

            // Запуск потоков
            usersThread.start();
            filesThread.start();

            // Ожидание завершения обоих потоков
            usersThread.join();
            filesThread.join();

            // Проверка ошибок
            if (error.get() != null) {
                throw new IllegalStateException(
                        "Ошибка при получении данных для отчёта",
                        error.get()
                );
            }

            // Формирование HTML
            String content = buildHtml(
                    usersCount.get(),
                    usersTime.get(),
                    files.get(),
                    filesTime.get(),
                    0
            );

            // Измерение общего времени после формирования HTML
            long totalTime = System.currentTimeMillis() - totalStartTime;

            // Повторное формирование HTML с итоговым временем
            content = buildHtml(
                    usersCount.get(),
                    usersTime.get(),
                    files.get(),
                    filesTime.get(),
                    totalTime
            );

            // Сохранение готового отчёта
            report.setContent(content);
            report.setStatus(ReportStatus.COMPLETED);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            report.setStatus(ReportStatus.ERROR);
        } catch (Exception e) {
            report.setStatus(ReportStatus.ERROR);
        } finally {
            reportRepository.save(report);
        }

        return CompletableFuture.completedFuture(null);
    }

    // Формирование HTML-отчёта
    private String buildHtml(
            long users,
            long usersTime,
            List<FileReportItem> files,
            long filesTime,
            long totalTime
    ) {

        var sb = new StringBuilder();

        sb.append("<html><head><meta charset='UTF-8'></head><body>");
        sb.append("<h1>Отчёт</h1>");
        sb.append("<p>Пользователей в системе: ")
                .append(users)
                .append(" (время вычисления: ")
                .append(usersTime)
                .append(" мс)</p>");
        sb.append("<p>Время получения списка файлов: ")
                .append(filesTime)
                .append(" мс</p>");
        sb.append("<table border='1'>");
        sb.append("<tr><th>ID</th><th>Имя файла</th></tr>");
        for (FileReportItem file : files) {
            sb.append("<tr><td>")
                    .append(file.id())
                    .append("</td><td>")
                    .append(HtmlUtils.htmlEscape(file.fileName()))
                    .append("</td></tr>");
        }
        sb.append("</table>");
        sb.append("<p>Общее время формирования отчёта: ")
                .append(totalTime)
                .append(" мс</p>");
        sb.append("</body></html>");

        return sb.toString();
    }

    // Объект для хранения данных о файле
    private record FileReportItem(
            Long id,
            String fileName,
            String contentType,
            Long fileSize
    ) {
    }
}
