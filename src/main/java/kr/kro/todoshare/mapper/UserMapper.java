package kr.kro.todoshare.mapper;

import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;

@Component
public class UserMapper {

    public User toEntity(UserCreateRequest request) {
        return User.builder()
                .loginId(request.loginId())
                .password(request.password())
                .nickname(request.nickname())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .tasks(new ArrayList<>())
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getLoginId(),
                user.getNickname(),
                user.getCreatedDate(),
                user.getModifiedDate(),
                user.getTasks().stream().map(this::toTaskInfo).toList()
        );
    }

    private UserResponse.TaskInfo toTaskInfo(Task task) {
        return new UserResponse.TaskInfo(
                task.getId(),
                task.getTitle(),
                task.getCompleted(),
                task.getDeadline(),
                task.getCreatedDate(),
                task.getModifiedDate()
        );
    }
}
