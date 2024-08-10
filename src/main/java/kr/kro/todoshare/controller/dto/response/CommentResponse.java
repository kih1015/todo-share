package kr.kro.todoshare.controller.dto.response;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        String content,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        WriterInfo writer,
        Long task
) {
    public record WriterInfo(
            Long id,
            String nickname
    ) {
    }
}
