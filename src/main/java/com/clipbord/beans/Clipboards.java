package com.clipbord.beans;


import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;

@Entity
public class Clipboards {

    @Id
    private String uniqueId;
    
    @Lob
    private String content;
    private LocalDateTime expiryTimestamp;

    public Clipboards() {
    }

    public Clipboards(String uniqueId, String content, LocalDateTime expiryTimestamp) {
        this.uniqueId = uniqueId;
        this.content = content;
        this.expiryTimestamp = expiryTimestamp;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getExpiryTimestamp() {
        return expiryTimestamp;
    }

    public void setExpiryTimestamp(LocalDateTime expiryTimestamp) {
        this.expiryTimestamp = expiryTimestamp;
    }
}

