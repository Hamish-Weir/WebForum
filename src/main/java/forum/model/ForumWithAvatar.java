package forum.model;
import forum.entities.Forum;

public class ForumWithAvatar {
    private Forum forum;
    private String avatarUrl;

    public ForumWithAvatar(Forum forum, String avatarUrl ) {
        this.forum = forum;
        this.avatarUrl = avatarUrl;
    }

    public Forum getForum() { return forum; }
    public String getLastPosterAvatarUrl() { return avatarUrl; }
}