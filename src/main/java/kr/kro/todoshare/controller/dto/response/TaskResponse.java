package kr.kro.todoshare.controller.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public record TaskResponse(
        Long id,
        String title,
        String content,
        Boolean completed,
        LocalDateTime deadline,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        WriterInfo writer,
        List<CommentInfo> comments,
        Long likesNum
) {

    public record WriterInfo(
            Long id,
            String nickname
    ) {
    }

    public record CommentInfo(
            Long id,
            String content,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate,
            WriterInfo writer
    ) {
    }
}
