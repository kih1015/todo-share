package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
import java.util.List;

@Tag(name = "Task API", description = "할일 관련 api")
@RestController
@RequestMapping("/tasks")
@AllArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @Operation(summary = "내 할일 생성", description = "할일을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "할일 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "title": "제목은 100글자 이하로 작성되어야 합니다.",
                      "content": "내용을 작성해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
    })
    @PostMapping()
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskCreateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        TaskResponse response = taskService.create(userId, request);
        return ResponseEntity.created(URI.create("/tasks/" + response.id())).body(response);
    }

    @Operation(summary = "할일 정보 요청", description = "할일 id로 상세 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 상세 정보 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "해당 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTask(
            @PathVariable Long id
    ) {
        TaskResponse response = taskService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "모든 할일 정보 요청", description = "모든 할일 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "모든 할일 상세 정보 불러오기 성공")
    })
    @GetMapping()
    public ResponseEntity<List<TaskResponse>> getTasks() {
        List<TaskResponse> response = taskService.getAll();
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 할일 정보 변경", description = "본인이 작성한 할일의 제목, 내용, 마감기한 등의 정보를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "할일 정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "title": "제목은 100글자 이하로 작성되어야 합니다.",
                      "content": "내용을 작성해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(examples = @ExampleObject(value = "권한이 없습니다."))),
            @ApiResponse(responseCode = "404", description = "해당 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
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

    @Operation(summary = "내 할일 완료 여부 변경", description = "본인이 작성한 할일의 완료 여부를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "완료 여부 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "completed": "완료 여부를 작성해야 합니다.",
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(examples = @ExampleObject(value = "권한이 없습니다."))),
            @ApiResponse(responseCode = "404", description = "해당 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
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

    @Operation(summary = "내 할일 삭제", description = "본인이 작성한 할일을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "할일 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "403", description = "권한 없음", content = @Content(examples = @ExampleObject(value = "권한이 없습니다."))),
            @ApiResponse(responseCode = "404", description = "해당 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
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
