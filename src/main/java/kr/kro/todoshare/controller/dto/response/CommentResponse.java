package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(Long id, String content, LocalDateTime createdDate, LocalDateTime modifiedDate,
                              Long writer, Long task) {

    public static CommentResponse from(Comment comment) {
        return new CommentResponse(comment.getId(), comment.getContent(), comment.getCreatedDate(), comment.getModifiedDate(), comment.getWriter().getId(), comment.getTask().getId());
    }
}
