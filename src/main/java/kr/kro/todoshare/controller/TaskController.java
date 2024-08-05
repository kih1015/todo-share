package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.TaskCompletedUpdateRequest;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        TaskResponse response = taskService.create(request, userId);
        return ResponseEntity.created(URI.create("/tasks/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id
    ) {
        TaskResponse response = taskService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskUpdateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        if (!taskService.getById(id).writer().id().equals(writerId)) {
            throw new AccessDeniedException();
        }
        TaskResponse response = taskService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}/completed")
    public ResponseEntity<TaskResponse> updateTaskCompleted(
            @PathVariable Long id,
            @Valid @RequestBody TaskCompletedUpdateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        if (!taskService.getById(id).writer().id().equals(writerId)) {
            throw new AccessDeniedException();
        }
        TaskResponse response = taskService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTask(
            @PathVariable Long id,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        if (!taskService.getById(id).writer().id().equals(writerId)) {
            throw new AccessDeniedException();
        }
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
