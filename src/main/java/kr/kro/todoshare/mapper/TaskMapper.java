package kr.kro.todoshare.mapper;

import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class TaskMapper {

    public Task toEntity(Long writerId, TaskCreateRequest request) {
        return Task.builder()
                .title(request.title())
                .content(request.content())
                .completed(false)
                .deadline(request.deadline())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .writer(User.builder().id(writerId).build())
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }

    public TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCompleted(),
                task.getDeadline(),
                task.getCreatedDate(),
                task.getModifiedDate(),
                toWriterInfo(task.getWriter()),
                task.getComments().stream().map(this::toCommentInfo).toList(),
                (long) task.getLikes().size()
        );
    }

    private TaskResponse.WriterInfo toWriterInfo(User writer) {
        return new TaskResponse.WriterInfo(writer.getId(), writer.getNickname());
    }

    private TaskResponse.CommentInfo toCommentInfo(Comment comment) {
        return new TaskResponse.CommentInfo(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedDate(),
                comment.getModifiedDate(),
                toWriterInfo(comment.getWriter())
        );
    }
}
