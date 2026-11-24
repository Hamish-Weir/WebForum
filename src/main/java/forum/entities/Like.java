package forum.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Table;

import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;


@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false, updatable = false)
    private Comment comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    private User user;

    // @Column(name = "created_at", nullable = false, updatable = false)
    // private LocalDateTime createdAt;    
    // @PrePersist 
    // protected void onCreate() { 
    //     createdAt = LocalDateTime.now(); 
    // }

    public Like() {
    }

    public Like(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public String toString() {
        return "Like{ID: " + id + ", Comment: " + comment.getId() + " User: " + user.getId() + "}";
    }

    public User getUser() {
        return user;
    }

}