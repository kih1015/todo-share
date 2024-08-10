package kr.kro.todoshare.controller.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        Long writer,
        Long task
) {
}
