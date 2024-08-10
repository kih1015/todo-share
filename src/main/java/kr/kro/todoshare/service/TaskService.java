package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.TaskCompletedUpdateRequest;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.mapper.TaskMapper;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor()
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final TaskMapper taskMapper;

    @Transactional
    public TaskResponse create(Long writerId, TaskCreateRequest request) {
        Task task = taskMapper.toEntity(writerId, request);
        return taskMapper.toResponse(taskRepository.save(task));
    }

    public TaskResponse getById(Long id) {
        Task task = taskRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return taskMapper.toResponse(task);
    }

    public List<TaskResponse> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.stream().map(taskMapper::toResponse).toList();
    }

    @Transactional
    public TaskResponse update(Long id, TaskUpdateRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        task.update(request.title(), request.content(), request.deadline(), request.completed());
        return taskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse update(Long id, TaskCompletedUpdateRequest request) {
        Task task = taskRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        task.update(request.completed());
        return taskMapper.toResponse(task);
    }

    @Transactional
    public void delete(Long id) {
        taskRepository.deleteById(id);
    }
}
