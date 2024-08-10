package kr.kro.todoshare.domain;

import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.mapper.LikeMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class LikeTest {

    @Test
    void of() {
        // given
        LikeMapper likeMapper = new LikeMapper();
        User user = mock(User.class);
        Task task = mock(Task.class);

        // when
        Like like = likeMapper.toEntity(user.getId(), new LikeCreateRequest(task.getId()));

        // then
        assertThat(like.getUser()).isEqualTo(user);
        assertThat(like.getTask()).isEqualTo(task);
    }
}