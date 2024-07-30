package kr.kro.todoshare.controller.dto.response;

import kr.kro.todoshare.domain.Like;

public record LikeResponse(
        Long id,
        Long user,
        Long task
) {

    public static LikeResponse from(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getUser().getId(),
                like.getTask().getId()
        );
    }
}
