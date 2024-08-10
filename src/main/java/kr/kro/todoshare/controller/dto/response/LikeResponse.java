package kr.kro.todoshare.controller.dto.response;

public record LikeResponse(
        Long id,
        Long user,
        Long task
) {
}
