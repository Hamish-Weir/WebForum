package forum.dao.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;

// import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import forum.dao.repositories.CommentRepository;
import forum.dao.repositories.LikeRepository;
import forum.dao.repositories.UserRepository;
import forum.entities.Comment;
import forum.entities.Like;
import forum.entities.User;
import forum.exceptions.DuplicateResourceException;
import forum.exceptions.ValidationException;
import jakarta.persistence.EntityNotFoundException;

@Service
@Transactional
public class LikeService {

    private final CommentRepository commentRepository;
    private final LikeRepository    likeRepository;
    private final UserRepository    userRepository;

    public LikeService(
        CommentRepository   commentRepository, 
        LikeRepository      likeRepository, 
        UserRepository      userRepository
    ) {
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
    }

    public List<User> getLikersByCommentId(Long commentId) {

        List<User> likers = likeRepository.findByCommentId(commentId).stream().map(
            like -> {
                return like.getUser();
            }
        ).toList();

        return likers;
    }

    public List<Like> getLikesByUserId(Long userId) {
        return likeRepository.findByUserId(userId);
    }


    public Like newLike(Long commentId, Long userId) {
        
        if (commentId == null                     ) {
            throw new ValidationException("commentId must not be blank");
        }
        if (userId  == null                     ) {
            throw new ValidationException("userId must not be blank");
        }

        Comment comment =   commentRepository.findById(commentId).orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        User user       =   userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
        
        Like like = new Like(comment, user);

        try {
            return likeRepository.save(like);
        } catch (DataIntegrityViolationException e) {
            // Race condition guard: two requests passed the pre-check simultaneously
            throw new DuplicateResourceException("User could not be created due to a conflict");
        }
    }
}

