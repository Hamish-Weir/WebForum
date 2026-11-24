package forum.model;
import java.util.List;

import forum.entities.Comment;
import forum.entities.User;

public class CommentWithAvatarAndLikes {
    private Comment comment;
    private String avatarUrl;
    private Long likes;
    private List<User> likers;

    public CommentWithAvatarAndLikes(Comment comment, String avatarUrl, Long likes, List<User> likers ) {
        this.comment = comment;
        this.avatarUrl = avatarUrl;
        this.likes = likes;
        this.likers = likers;
    }

    public Comment getComment() { return comment; }
    public String getAvatarUrl() { return avatarUrl; }
    public Long getLikes() { return likes; }
    public List<User> getLikers() { return likers; }
}