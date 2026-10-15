package ru.mikhail.NauJava.cli;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.mikhail.NauJava.service.FileService;

@Component
public class CommandProcessor {

    private final FileService fileService;

    @Autowired
    public CommandProcessor(FileService fileService) {
        this.fileService = fileService;
    }

    public void processCommand(String input) {
        if (input == null || input.isBlank()) {
            return;
        }

        var cmd = input.trim().split(" ");
        var command = cmd[0].toLowerCase();

        try {
            switch (command) {
                case "upload" -> {
                    var id = Long.valueOf(cmd[1]);
                    var name = cmd[2];
                    var size = Long.valueOf(cmd[3]);
                    fileService.uploadFile(id, name, size);
                    System.out.println("Файл успешно загружен!");
                }
                case "get" -> {
                    var id = Long.valueOf(cmd[1]);
                    var file = fileService.findById(id);
                    if (file != null)
                        System.out.println(file);
                    else
                        System.out.println("Файл с ID " + id + " не найден");
                }
                case "download" -> {
                    var url = cmd[1];
                    var file = fileService.downloadByUrl(url);
                    if (file != null)
                        System.out.println("Скачивание файла: " + file.getName() + " (Размер: " + file.getSize() + " байт)");
                    else
                        System.out.println("Файл по ссылке не найден");
                }
                case "delete" -> {
                    var id = Long.valueOf(cmd[1]);
                    var deleted = fileService.deleteById(id);
                    if (deleted)
                        System.out.println("Файл с ID " + id + " удален");
                    else
                        System.out.println("Файл с ID " + id + " не найден");
                }
                default -> System.out.println("Введена неизвестная команда...");
            }
        } catch (ArrayIndexOutOfBoundsException | NumberFormatException e) {
            System.out.println("Ошибка в аргументах команды, проверьте формат ввода");
        }
    }
}
