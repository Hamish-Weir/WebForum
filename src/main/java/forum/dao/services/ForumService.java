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
import forum.entities.Forum;
import forum.exceptions.DuplicateResourceException;
import forum.exceptions.ValidationException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class ForumService {

    private final ForumRepository forumRepository;

    public ForumService(
        ForumRepository forumRepository
    ) {
        this.forumRepository = forumRepository;
    }

    public Forum getForum(Long id) {
        if (id == null) {
            throw new ValidationException("Id must not be blank");
        }

        return forumRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Forum not found: " + id));
    }

    public Page<Forum> getForums(int page, int pageSize) {

        if (page < 1) throw new IllegalArgumentException("Page must be >= 1");
        if (pageSize < 1) throw new IllegalArgumentException("PageSize must be >= 1");
        
        Pageable pageable = PageRequest.of(page-1, pageSize, Sort.by("id").ascending());
        return forumRepository.findAll(pageable);
    }

    public Forum newForum(String title, String body) {

        if (title   == null || title.isBlank()  ) {
            throw new ValidationException("title must not be blank");
        }
        if (body    == null || body.isBlank()   ) {
            throw new ValidationException("body must not be blank");
        }

        if (forumRepository.existsByTitleIgnoreCase(title)) {
            throw new DuplicateResourceException("Title already taken: " + title);
        }

        Forum forum = new Forum(title, body);

        try {
            return forumRepository.save(forum);
        } catch (DataIntegrityViolationException e) {
            // Race condition guard: two requests passed the pre-check simultaneously
            throw new DuplicateResourceException("User could not be created due to a conflict");
        }
    }

    public long count() {
        return forumRepository.count();
    }
}
