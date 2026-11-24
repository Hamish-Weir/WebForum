package forum.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.PrePersist;

import java.time.LocalDateTime;


@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "thread_id", nullable = false)
    private Thread thread;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    @Column(length = 65535, nullable = false)
    private String body;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;


    // @OneToMany(mappedBy = "likes", cascade = CascadeType.ALL)
    // private List<Like> likes;

    
    @PrePersist 
    protected void onCreate() { 
        createdAt = LocalDateTime.now(); 
    }

    public Comment() {
    }

    public Comment(Thread thread, User user, String body) {
        this.thread = thread;
        this.user = user;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }



    public String toString() {
        return "Comment{ID: " + id + ", Thread: " + thread.getId() + " User: " + user.getId() + ", Body: " + body + ", Date:" + createdAt + "}";
    }

}