package forum.dao.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import forum.dao.repositories.CommentRepository;
import forum.dao.repositories.ThreadRepository;
import forum.dao.repositories.UserRepository;
import forum.entities.Comment;
import forum.entities.Thread;
import forum.entities.User;
import forum.exceptions.DuplicateResourceException;
import forum.exceptions.ValidationException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final ThreadRepository  threadRepository;
    private final UserRepository    userRepository;

    public CommentService(
        CommentRepository commentRepository, 
        ThreadRepository  threadRepository, 
        UserRepository    userRepository
    ) {
        this.commentRepository = commentRepository;
        this.threadRepository = threadRepository;
        this.userRepository = userRepository;
    }

    public Page<Comment> getCommentsByThreadIdPage(Long forumId, int page, int pageSize) {
        
        if (page < 1) throw new IllegalArgumentException("Page must be >= 1");
        if (pageSize < 1) throw new IllegalArgumentException("PageSize must be >= 1");

        commentRepository.findById(forumId).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + forumId));

        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("id").ascending());
        return commentRepository.findByThreadId(forumId, pageable);
    }


    public Comment newComment(Long threadId, Long userId, String body) {
        
        if (threadId == null                     ) {
            throw new ValidationException("threadId must not be blank");
        }
        if (userId  == null                     ) {
            throw new ValidationException("userId must not be blank");
        }

        if (body   == null || body.isBlank()  ) {
            throw new ValidationException("title must not be blank");
        }

        Thread thread =   threadRepository.findById(threadId).orElseThrow(() -> new EntityNotFoundException("Forum not found"));
        User user   =   userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Comment comment = new Comment(thread, user, body);

        try {
            return commentRepository.save(comment);
        } catch (DataIntegrityViolationException e) {
            // Race condition guard: two requests passed the pre-check simultaneously
            throw new DuplicateResourceException("User could not be created due to a conflict");
        }
    }

}
