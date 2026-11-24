package forum.dao.repositories;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import forum.entities.Thread;

public interface ThreadRepository extends JpaRepository<Thread, Long> {
    
    Page<Thread> findByForumId(Long forumId, Pageable pageable);
}