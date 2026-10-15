package ru.mikhail.NauJava.entities;

public class File {
    private long id;
    private String name;
    private String downloadURL;
    private long size;

    public File() {
    }

    public File(long id, String name, long size) {
        this.id = id;
        this.name = name;
        this.size = size;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDownloadURL() {
        return downloadURL;
    }

    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Имя: " + name + ", ссылка: " + downloadURL + ", размер: " + size + " байт";
    }
}
