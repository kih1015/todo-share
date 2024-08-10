package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "Like API", description = "좋아요 관련 api")
@RestController
@RequestMapping("/likes")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "좋아요 생성", description = "타인의 할일에 좋아요를 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "좋아요 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다."))),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "task": "할일의 id를 입력해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "404", description = "좋아요를 생성할 할일이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다."))),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 누름", content = @Content(examples = @ExampleObject(value = "이미 좋아요를 눌렀습니다.")))
    })
    @PostMapping()
    public ResponseEntity<LikeResponse> createLike(
            @Valid @RequestBody LikeCreateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        checkLogin(userId);
        LikeResponse response = likeService.create(userId, request);
        return ResponseEntity.created(URI.create("/likes/" + response.id())).body(response);
    }

    private static void checkLogin(Long userId) {
        if (userId == null) {
            throw new AuthenticationException();
        }
    }
}
