package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.request.CommentUpdateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Comment API", description = "댓글 관련 api")
@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 생성", description = "댓글을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "댓글 생성 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "content": "빈 댓글은 작성할 수 없습니다.",
                      "task": "할일의 id를 입력해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "404", description = "댓글을 등록할 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        checkLogin(writerId);
        CommentResponse response = commentService.create(writerId, request);
        return ResponseEntity.created(URI.create("/comments/" + response.id())).body(response);
    }

    @Operation(summary = "내 댓글 내용 변경", description = "댓글 내용을 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "댓글 내용 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "content": "빈 댓글은 작성할 수 없습니다.",
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "403", description = "댓글 수정 권한 없음", content = @Content(examples = @ExampleObject(value = "권한이 없습니다."))),
            @ApiResponse(responseCode = "404", description = "수정할 댓글이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        checkLogin(writerId);
        checkAccessAuthor(writerId, id);
        CommentResponse response = commentService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 댓글 삭제", description = "댓글 내용을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "403", description = "댓글 삭제 권한 없음", content = @Content(examples = @ExampleObject(value = "권한이 없습니다."))),
            @ApiResponse(responseCode = "404", description = "삭제할 댓글이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteComment(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @PathVariable Long id
    ) {
        checkLogin(writerId);
        checkAccessAuthor(writerId, id);
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    private void checkAccessAuthor(Long userId, Long id) {
        if (!commentService.getById(id).writer().equals(userId)) {
            throw new AccessDeniedException();
        }
    }

    private static void checkLogin(Long userId) {
        if (userId == null) {
            throw new AuthenticationException();
        }
    }
}
