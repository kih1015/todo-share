package kr.kro.todoshare.controller.dto.request;

public record CommentCreateRequest(
        String content,
        Long task
) {
}
