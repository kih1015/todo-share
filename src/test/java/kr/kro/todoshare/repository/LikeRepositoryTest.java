package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class LikeRepositoryTest {

    @Autowired
    private LikeRepository likeRepository;

    @Test
    @DisplayName("좋아요 저장 테스트")
    void saveLike() {
        // given
        User user = User.builder()
                .loginId("testlogin")
                .password("password")
                .nickname("테스트닉네임")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();

        Task task = Task.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .completed(false)
                .deadline(LocalDateTime.now().plusDays(1))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .build();

        Like like = Like.builder()
                .user(user)
                .task(task)
                .build();

        // when
        Like savedLike = likeRepository.save(like);

        // then
        assertThat(savedLike).isNotNull();
        assertThat(savedLike.getId()).isNotNull();
        assertThat(savedLike.getUser()).isEqualTo(user);
        assertThat(savedLike.getTask()).isEqualTo(task);
    }

    @Test
    @DisplayName("특정 작업에 대한 좋아요 조회 테스트")
    void findAllByTask() {
        // given
        User user = User.builder()
                .loginId("testlogin")
                .password("password")
                .nickname("테스트닉네임")
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();

        Task task = Task.builder()
                .title("테스트 제목")
                .content("테스트 내용")
                .completed(false)
                .deadline(LocalDateTime.now().plusDays(1))
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .writer(user)
                .build();

        Like like1 = Like.builder()
                .user(user)
                .task(task)
                .build();
        Like like2 = Like.builder()
                .user(user)
                .task(task)
                .build();
        likeRepository.save(like1);
        likeRepository.save(like2);

        // when
        List<Like> likes = likeRepository.findAllByTask(task);

        // then
        assertThat(likes).isNotEmpty();
        assertThat(likes).hasSize(2);
        assertThat(likes).extracting("user").containsExactlyInAnyOrder(user, user);
        assertThat(likes).extracting("task").containsExactlyInAnyOrder(task, task);
    }
}
