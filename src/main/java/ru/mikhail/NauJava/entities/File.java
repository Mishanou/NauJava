package ru.mikhail.NauJava.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "files")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fileName;

    @Column
    private String contentType;

    @Column(nullable = false)
    private Long fileSize;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User uploader;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<ShareLink> shareLinks;

    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL)
    private List<DownloadStat> downloadStats;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }

    public String getContentType() { return contentType; }
    public void setContentType(String contentType) { this.contentType = contentType; }

    public Long getFileSize() { return fileSize; }
    public void setFileSize(Long fileSize) { this.fileSize = fileSize; }

    public User getUploader() { return uploader; }
    public void setUploader(User uploader) { this.uploader = uploader; }

    public List<ShareLink> getShareLinks() { return shareLinks; }
    public void setShareLinks(List<ShareLink> shareLinks) { this.shareLinks = shareLinks; }

    public List<DownloadStat> getDownloadStats() { return downloadStats; }
    public void setDownloadStats(List<DownloadStat> downloadStats) { this.downloadStats = downloadStats; }
}