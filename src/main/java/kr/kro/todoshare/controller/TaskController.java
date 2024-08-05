package kr.kro.todoshare.controller;

import jakarta.servlet.http.HttpSession;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
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
    public ResponseEntity<TaskResponse> createTask(@RequestBody TaskCreateRequest request, HttpSession session) {
        TaskResponse response = taskService.create(request, (Long) session.getAttribute("userId"));
        return ResponseEntity.created(URI.create("/tasks/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTasks(@PathVariable Long id) {
        TaskResponse response = taskService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(@PathVariable Long id, @RequestBody TaskUpdateRequest request) {
        TaskResponse response = taskService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TaskResponse> deleteTask(@PathVariable Long id) {
        taskService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
