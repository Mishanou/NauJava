package ru.mikhail.NauJava.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "download_stats")
public class DownloadStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime downloadDate;

    @Column
    private String ipAddress;

    @Column
    private String userAgent;

    @ManyToOne
    @JoinColumn(name = "file_id", nullable = false)
    private File file;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDownloadDate() { return downloadDate; }
    public void setDownloadDate(LocalDateTime downloadDate) { this.downloadDate = downloadDate; }

    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }

    public String getUserAgent() { return userAgent; }
    public void setUserAgent(String userAgent) { this.userAgent = userAgent; }

    public File getFile() { return file; }
    public void setFile(File file) { this.file = file; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}