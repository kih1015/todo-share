package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;

import java.time.LocalDateTime;
import java.util.List;

public record UserResponse(
        Long id,
        String loginId,
        String nickname,
        LocalDateTime createdDate,
        LocalDateTime modifiedDate,
        List<TaskInfo> task
) {

    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.getCreatedDate(),
                user.getModifiedDate(),
                user.getTasks().stream().map(TaskInfo::from).toList()
        );
    }

    private record TaskInfo(
            Long id,
            String title,
            Boolean completed,
            LocalDateTime deadline,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate
    ) {

        public static TaskInfo from(Task task) {
            return new TaskInfo(
                    task.getId(),
                    task.getTitle(),
                    task.getCompleted(),
                    task.getDeadline(),
                    task.getCreatedDate(),
                    task.getModifiedDate()
            );
        }
    }
}
