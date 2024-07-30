package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Comment;
import org.springframework.data.repository.Repository;

public interface CommentRepository extends Repository<Comment, Long> {

    Comment findById(Long id);

    Comment save(Comment comment);

    void deleteById(Long id);
}
