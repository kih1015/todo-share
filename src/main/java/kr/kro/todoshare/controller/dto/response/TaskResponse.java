package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.Task;

import java.time.LocalDateTime;

public record TaskResponse(Long id, String title, String content, Boolean completed, LocalDateTime deadline,
                           LocalDateTime createdDate, LocalDateTime modifiedDate, Long writer) {

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCompleted(),
                task.getDeadline(),
                task.getCreatedDate(),
                task.getModifiedDate(),
                task.getWriter().getId()
        );
    }
}
