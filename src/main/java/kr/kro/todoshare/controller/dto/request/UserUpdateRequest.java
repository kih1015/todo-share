package kr.kro.todoshare.controller.dto.request;

public record UserUpdateRequest(
        String nickname,
        String password
) {
}
