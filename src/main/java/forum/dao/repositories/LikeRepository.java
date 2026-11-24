package forum.dao.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import forum.entities.Like;

public interface LikeRepository extends CrudRepository<Like, Long> {

    List<Like> findByCommentId(Long commentId);

    List<Like> findByUserId(Long commentId);
}