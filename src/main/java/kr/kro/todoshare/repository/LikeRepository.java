package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.Task;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface LikeRepository extends Repository<Like, Long> {

    List<Like> findAllByTask(Task task);

    Like save(Like like);
}
