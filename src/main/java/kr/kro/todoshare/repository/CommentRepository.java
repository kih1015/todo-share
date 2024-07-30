package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.domain.User;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface CommentRepository extends Repository<Comment, Long> {

    Optional<Comment> findById(Long id);

    Comment save(Comment comment);

    void deleteById(Long id);
}
