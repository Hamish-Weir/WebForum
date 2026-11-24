package forum.model;
import forum.entities.Thread;

public class ThreadWithAvatar {
    private Thread thread;
    private String creatorAvatarUrl;
    private String lastPosterAvatarUrl;

    public ThreadWithAvatar(Thread thread, String creatorAvatarUrl, String lastPosterAvatarUrl ) {
        this.thread = thread;
        this.creatorAvatarUrl = creatorAvatarUrl;
        this.lastPosterAvatarUrl = lastPosterAvatarUrl;
    }

    public Thread getThread() { return thread; }
    public String getCreatorAvatarUrl() { return creatorAvatarUrl; }
    public String getLastPosterAvatarUrl() { return lastPosterAvatarUrl; }
}