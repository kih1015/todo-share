package kr.kro.todoshare.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;

class LikeTest {

    @Test
    void of() {
        // given
        User user = mock(User.class);
        Task task = mock(Task.class);

        // when
        Like like = Like.of(user, task);

        // then
        assertThat(like.getUser()).isEqualTo(user);
        assertThat(like.getTask()).isEqualTo(task);
    }
}