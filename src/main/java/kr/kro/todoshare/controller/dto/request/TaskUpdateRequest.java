package kr.kro.todoshare.controller.dto.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record TaskUpdateRequest(
        @NotBlank(message = "제목을 작성해야 합니다.")
        @Size(max = 100, message = "제목은 100글자 이하로 작성되어야 합니다.")
        String title,

        @NotBlank(message = "내용을 작성해야 합니다.")
        String content,

        @NotNull(message = "마감기한을 설정해야 합니다.")
        @Future(message = "마감기한은 미래여야 합니다.")
        LocalDateTime deadline,

        @NotNull(message = "완료 여부를 작성해야 합니다.")
        Boolean completed
) {
}
