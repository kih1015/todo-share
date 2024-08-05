package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CommentCreateRequest(
        @NotBlank(message = "빈 댓글은 작성할 수 없습니다.")
        String content,

        @NotNull(message = "작업의 id를 입력해야 합니다.")
        Long task
) {
}
