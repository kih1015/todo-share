package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Task;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends Repository<Task, Long> {

    List<Task> findAll();

    Optional<Task> findById(Long id);

    Task save(Task task);

    void deleteById(Long id);
}
