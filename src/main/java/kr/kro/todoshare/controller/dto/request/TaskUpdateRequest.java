package kr.kro.todoshare.controller.dto.request;

import java.time.LocalDateTime;

public record TaskUpdateRequest(String title, String content, LocalDateTime deadline, Boolean completed) {
}
