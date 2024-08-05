package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record LikeCreateRequest(
        @NotNull(message = "작업의 id를 입력해야 합니다.")
        Long task
) {
}
