package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.repository.TaskRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Transactional
    public TaskResponse create(TaskCreateRequest taskCreateRequest) {
        Task task = Task.builder()
                .title(taskCreateRequest.title())
                .content(taskCreateRequest.content())
                .deadline(taskCreateRequest.deadline())
                .build();
        return TaskResponse.from(taskRepository.save(task));
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id);
        return TaskResponse.from(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskUpdateRequest taskUpdateRequest) {
        Task task = taskRepository.findById(id);
        task.update(taskUpdateRequest.title(), taskUpdateRequest.content(), taskUpdateRequest.deadline(), taskUpdateRequest.completed());
        return TaskResponse.from(task);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
