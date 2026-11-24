package forum.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import forum.entities.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    Page<Comment> findByThreadId(Long threadId, Pageable pageable);
}