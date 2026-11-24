package forum.dao.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import forum.entities.Forum;

public interface ForumRepository extends JpaRepository<Forum, Long> {

    boolean existsByTitleIgnoreCase(String title);
    Page<Forum> findAll(Pageable pageable);
}