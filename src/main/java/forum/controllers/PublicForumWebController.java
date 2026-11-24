package forum.controllers;

import org.springframework.ui.Model;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import forum.dao.services.CommentService;
import forum.dao.services.ForumService;
import forum.dao.services.ImageService;
import forum.dao.services.LikeService;
import forum.dao.services.ThreadService;
import forum.entities.Forum;
import forum.entities.Thread;
import forum.entities.User;
import forum.entities.Comment;
import forum.entities.Like;
import forum.model.CommentWithAvatarAndLikes;
import forum.model.ForumWithAvatar;
import forum.model.ThreadWithAvatar;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;

@Controller
@RequestMapping(value = "/forums", produces = MediaType.TEXT_HTML_VALUE)
public class PublicForumWebController {
    
    private final CommentService commentService;
    private final ForumService forumService;
    private final LikeService likeService;
    private final ThreadService threadService;
    private final ImageService imageService;
    
    public PublicForumWebController(CommentService commentService, ForumService forumService, LikeService likeService,ThreadService threadService, ImageService imageService) {
        this.commentService = commentService;
        this.forumService = forumService;
        this.likeService = likeService;
        this.threadService = threadService;
        this.imageService = imageService;
    }

    // Forums
    @GetMapping()
    public String listForums(Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {

        model.addAttribute("pageName", "Forums");
        model.addAttribute("pageDescription", "List of Forums");

        try {
            Page<Forum> page = forumService.getForums(pageNum, pageSize);

            List<ForumWithAvatar> forumWithAvatar = page.getContent().stream()
                .map(forum -> {
                    String avatarUrl = forum.getLastPoster() != null 
                        ? imageService.getImage(forum.getLastPoster().getPfpPath())
                        : null;
                    return new ForumWithAvatar(forum, avatarUrl);
                })
                .toList();

    
            model.addAttribute("forums", forumWithAvatar);
            model.addAttribute("page", page);
            

            return "forums/home";

        } catch (EntityNotFoundException e) {

            model.addAttribute("error", e.getMessage());

            return "error";
        }


        
    }

    @GetMapping("/forum/{forumId}")
    public String viewForum(@PathVariable Long forumId, Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {

        try {
            Forum forum = forumService.getForum(forumId);
            Page<Thread> page = threadService.getThreadsByForumIdPage(forumId, pageNum, pageSize);
            
            List<ThreadWithAvatar> threadWithAvatar = page.getContent().stream()
                .map(thread -> {
                    String creatorAvatarUrl = thread.getUser() != null
                        ? imageService.getImage(thread.getUser().getPfpPath())
                        : null;

                    String lastPosterAvatarUrl = thread.getLastPoster() != null
                        ? imageService.getImage(thread.getLastPoster().getPfpPath())
                        : null;

                    return new ThreadWithAvatar(thread, creatorAvatarUrl, lastPosterAvatarUrl);})
                .toList();

    
            model.addAttribute("forumName",         forum.getTitle());
            model.addAttribute("forumDescription",  forum.getBody());
    
            model.addAttribute("threads", threadWithAvatar);
            model.addAttribute("page", page);
            
    
            return "forums/forum";

        } catch (EntityNotFoundException e) {

            model.addAttribute("error", e.getMessage());

            return "error";
        }
    }

    @GetMapping("/thread/{threadId}")
    public String viewThread(@PathVariable Long threadId, Model model, @RequestParam(defaultValue = "1") int pageNum, @RequestParam(defaultValue = "10") int pageSize) {

        try {
            Thread thread = threadService.getThread(threadId);
            Page<Comment> page = commentService.getCommentsByThreadIdPage(threadId, pageNum, pageSize);
            
            List<CommentWithAvatarAndLikes> threadWithAvatar = page.getContent().stream()
                .map(comment -> {
                    String avatarUrl = comment.getUser() != null
                        ? imageService.getImage(comment.getUser().getPfpPath())
                        : null;

                    List<User> likers = likeService.getLikersByCommentId(comment.getId());
                    Long likes = Long.valueOf(likers.size());

                    return new CommentWithAvatarAndLikes(comment, avatarUrl, likes, likers);})
                .toList();

    
            model.addAttribute("threadName",         thread.getTitle());
            model.addAttribute("threadDescription",  thread.getBody());
    
            model.addAttribute("comments", threadWithAvatar);
            model.addAttribute("page", page);
            
    
            return "forums/thread";

        } catch (EntityNotFoundException e) {

            model.addAttribute("error", e.getMessage());

            return "error";
        }
    }


    @GetMapping("/thread/{tId}")
    public String viewThread(@PathVariable Long tId, Model model) {
        System.out.println("HERE");
        model.addAttribute("thread", tId);

        return "forums/thread";
    }


}