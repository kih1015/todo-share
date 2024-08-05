package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public TaskResponse create(TaskCreateRequest request, Long writerId) {
        Task task = Task.of(request, userRepository.findById(writerId));
        return TaskResponse.from(taskRepository.save(task));
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id);
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id);
        task.update(request.title(), request.content(), request.deadline(), request.completed());
        return TaskResponse.from(task);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
