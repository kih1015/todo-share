package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.NotBlank;

public record CommentUpdateRequest(
        @NotBlank(message = "빈 댓글은 작성할 수 없습니다.")
        String content
) {
}
