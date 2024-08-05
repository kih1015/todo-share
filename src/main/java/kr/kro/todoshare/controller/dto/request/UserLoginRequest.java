package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserLoginRequest(
        @NotBlank(message = "아이디를 작성해야 합니다.")
        @Size(min = 4, max = 20, message = "로그인 아이디는 4자 이상 20자 이하여야 합니다.")
        String loginId,

        @NotBlank(message = "비밀번호를 작성해야 합니다.")
        @Size(min = 8, message = "비밀번호는 8자 이상이어야 합니다.")
        String password
) {
}
