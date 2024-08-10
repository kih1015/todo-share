package kr.kro.todoshare.controller.dto.response;

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

    public record TaskInfo(
            Long id,
            String title,
            Boolean completed,
            LocalDateTime deadline,
            LocalDateTime createdDate,
            LocalDateTime modifiedDate
    ) {
    }
}
