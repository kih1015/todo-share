package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.User;

import java.time.LocalDateTime;

public record UserResponse(Long id, String loginId, String nickname, LocalDateTime createdDate,
                           LocalDateTime modifiedDate) {

    public static UserResponse fromUser(User user) {
        return new UserResponse(user.getId(), user.getLoginId(), user.getNickname(), user.getCreatedDate(), user.getModifiedDate());
    }
}
