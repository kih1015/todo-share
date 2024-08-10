package kr.kro.todoshare.mapper;

import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.springframework.stereotype.Component;

@Component
public class LikeMapper {

    public Like toEntity(Long userId, LikeCreateRequest request) {
        return Like.builder()
                .user(User.builder().id(userId).build())
                .task(Task.builder().id(request.task()).build())
                .build();
    }

    public LikeResponse toResponse(Like like) {
        return new LikeResponse(
                like.getId(),
                like.getUser().getId(),
                like.getTask().getId()
        );
    }
}
