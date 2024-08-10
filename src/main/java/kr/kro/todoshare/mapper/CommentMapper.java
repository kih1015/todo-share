package kr.kro.todoshare.mapper;

import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CommentMapper {

    public Comment toEntity(Long writerId, CommentCreateRequest request) {
        return Comment.builder()
                .content(request.content())
                .writer(User.builder().id(writerId).build())
                .task(Task.builder().id(request.task()).build())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }

    public CommentResponse toResponse(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getModifiedDate(),
                comment.getWriter().getId(),
                comment.getTask().getId()
        );
    }
}
