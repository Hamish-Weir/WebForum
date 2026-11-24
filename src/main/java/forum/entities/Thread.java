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
@Table(name = "threads")
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forum_id", nullable = false, updatable = false)
    private Forum forum;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

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


    // @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    // private List<Comment> comments;

    
    @PrePersist 
    protected void onCreate() { 
        createdAt = LocalDateTime.now(); 
        lastPostDate = createdAt;
        postCount = 0l;
        lastPoster = user;
    }


    public Thread() {
    }

    public Thread(Forum forum, User user, String title, String body) {
        this.forum = forum;
        this.user = user;
        this.title = title;
        this.body = body;
    }

    public Long getId() {
        return id;
    }

    public Forum getForum() {
        return forum;
    }

    public User getUser() {
        return user;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
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
        return "Thread{ID: " + id + ", Forum: " + forum.getId() + " User: " + user.getId() + ", Body: " + body + ", Date:" + createdAt + "}";
    }

}