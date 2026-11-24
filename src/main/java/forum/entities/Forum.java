package forum.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Null;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.CascadeType;

import java.time.LocalDateTime;
import java.util.List;



@Entity
@Table(name = "forums")
public class Forum {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 255, nullable = false)
    private String title;

    @Lob
    @Column(length = 65535, nullable = false)
    private String body;



    @Column(nullable = false)
    private Long postCount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_poster_id")
    private User lastPoster;

    @Column(nullable = false)
    private LocalDateTime lastPostDate;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @PrePersist 
    protected void onCreate() { 
        createdAt = LocalDateTime.now(); 
        lastPostDate = createdAt;
        postCount = 0l;
    }

    public Forum() {
    }

    public Forum(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Long getId() {
        return id;
    }

    public Long getPostCount() {
        return postCount;
    }

    public LocalDateTime getLastPostDate() {
        return lastPostDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public User getLastPoster() {
        return lastPoster;
    }

    public String toString() {
        return "Forum{ID: " + id + ", Title: " + title + ", Body: " + body + ", Date:" + createdAt + "}";
    }

}