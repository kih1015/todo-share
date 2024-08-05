package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;

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

    public static TaskResponse from(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getContent(),
                task.getCompleted(),
                task.getDeadline(),
                task.getCreatedDate(),
                task.getModifiedDate(),
                WriterInfo.from(task.getWriter()),
                task.getComments().stream().map(CommentInfo::from).toList(),
                (long) task.getLikes().size()
        );
    }

    private record WriterInfo(
            Long id,
            String nickname
    ) {

        public static WriterInfo from(User writer) {
            return new WriterInfo(writer.getId(), writer.getNickname());
        }
    }

    private record CommentInfo(
            Long id,
            String content,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate,
            WriterInfo writer
    ) {

        public static CommentInfo from(Comment comment) {
            return new CommentInfo(
                    comment.getId(),
                    comment.getContent(),
                    comment.getCreatedDate(),
                    comment.getModifiedDate(),
                    WriterInfo.from(comment.getWriter())
            );
        }
    }
}
