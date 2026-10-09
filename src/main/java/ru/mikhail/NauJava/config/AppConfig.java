package ru.mikhail.NauJava.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ru.mikhail.NauJava.cli.CommandProcessor;
import ru.mikhail.NauJava.entities.File;

@Configuration
public class AppConfig {
    @Value("${app.name}")
    private String appName;

    @Value("${app.version}")
    private String appVersion;

    @PostConstruct
    public void init() {
        System.out.println("==========================================");
        System.out.println("Запуск приложения: " + appName);
        System.out.println("Версия: " + appVersion);
        System.out.println("==========================================");
    }

    @Bean
    public List<File> fileContainer() {
        return new ArrayList<>();
    }

    @Bean
    public CommandLineRunner commandScanner(CommandProcessor commandProcessor) {
        return args -> {
            try (var scanner = new Scanner(System.in)) {
                System.out.println("""
                        Добро пожаловать! Список доступных команд:
                        upload [id] [filename] [file size] - загрузить файл
                        get [id] - получить файл по его id
                        download [url] - скачать файл по ссылке
                        delete [id] - удалить файл
                        exit - выйти из приложения
                        
                        Введите команду:""");
                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
