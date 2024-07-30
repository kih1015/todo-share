package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface LikeRepository extends Repository<Like, Long> {

    List<Like> findAllByTask(Task task);

    Like save(Like like);

    void deleteById(Long id);
}
