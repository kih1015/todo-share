package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.NotNull;

public record TaskCompletedUpdateRequest(
        @NotNull(message = "완료 여부를 작성해야 합니다.")
        Boolean completed
) {
}
