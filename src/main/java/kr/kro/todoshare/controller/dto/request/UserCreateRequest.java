package kr.kro.todoshare.controller.dto.request;

public record UserCreateRequest(
        String loginId,
        String password,
        String nickname
) {
}
