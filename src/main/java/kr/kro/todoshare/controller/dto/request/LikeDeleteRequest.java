package kr.kro.todoshare.controller.dto.request;

public record LikeDeleteRequest(
        Long user,
        Long task
) {
}
