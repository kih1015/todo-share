package kr.kro.todoshare.controller.dto.request;

import kr.kro.todoshare.domain.User;

public record UserCreateRequest(
        String loginId,
        String password,
        String nickname
) {
}
