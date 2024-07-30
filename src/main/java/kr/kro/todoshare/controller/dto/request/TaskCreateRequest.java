package kr.kro.todoshare.controller.dto.request;

import java.time.LocalDateTime;

public record TaskCreateRequest(String title, String content, LocalDateTime deadline, Long writer) {
}
