package kr.kro.todoshare.controller.dto.request;

public record UserLoginRequest(
        String loginId,
        String password
) {
}
