package forum.dao.services;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

// import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import forum.dao.repositories.ForumRepository;
import forum.dao.repositories.ThreadRepository;
import forum.dao.repositories.UserRepository;
import forum.entities.Forum;
import forum.entities.Thread;
import forum.entities.User;
import forum.exceptions.DuplicateResourceException;
import forum.exceptions.ValidationException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ThreadService {

    private final ForumRepository forumRepository;
    private final ThreadRepository  threadRepository;
    private final UserRepository    userRepository;

    public ThreadService(
        ForumRepository forumRepository, 
        ThreadRepository  threadRepository, 
        UserRepository    userRepository
    ) {
        this.forumRepository = forumRepository;
        this.threadRepository = threadRepository;
        this.userRepository = userRepository;
    }

    public Page<Thread> getThreadsByForumIdPage(Long forumId, int page, int pageSize) {

        if (page < 1) throw new IllegalArgumentException("Page must be >= 1");
        if (pageSize < 1) throw new IllegalArgumentException("PageSize must be >= 1");

        forumRepository.findById(forumId).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + forumId));

        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("id").ascending());
        return threadRepository.findByForumId(forumId, pageable);
    }

    public Forum getForum(Long id) {
        if (id == null) {
            throw new ValidationException("Id must not be blank");
        }

        return forumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + id));
    }

    public Thread getThread(Long id) {
        if (id == null) {
            throw new ValidationException("Id must not be blank");
        }

        return threadRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + id));
    }


    public Thread newThread(Long forumId, Long userId, String title, String body) {
        if (forumId == null                     ) {
            throw new ValidationException("forumId must not be blank");
        }
        if (userId  == null                     ) {
            throw new ValidationException("userId must not be blank");
        }
        if (title   == null || title.isBlank()  ) {
            throw new ValidationException("title must not be blank");
        }
        if (body    == null || body.isBlank()   ) {
            throw new ValidationException("body must not be blank");
        }

        Forum forum =   forumRepository.findById(forumId).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + forumId));
        User user   =   userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        
        Thread thread = new Thread(forum, user, title, body);

        try {
            return threadRepository.save(thread);
        } catch (DataIntegrityViolationException e) {
            // Race condition guard: two requests passed the pre-check simultaneously
            throw new DuplicateResourceException("User could not be created due to a conflict");
        }
    }

}
