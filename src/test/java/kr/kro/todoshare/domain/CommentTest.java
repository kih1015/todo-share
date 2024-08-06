package kr.kro.todoshare.domain;

import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CommentTest {

    @Test
    @DisplayName("댓글 엔티티 업데이트 테스트")
    void update() {
        // given
        User user = mock(User.class);
        Task task = mock(Task.class);
        Comment comment = Comment.builder()
                .content("처음 댓글")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .task(task)
                .build();
        String newContent = "댓글 업데이트 테스트";

        // when
        comment.update(newContent);

        // then
        assertThat(comment.getContent()).isEqualTo(newContent);
        assertThat(comment.getModifiedDate()).isNotNull();
        assertThat(comment.getModifiedDate()).isAfter(comment.getCreatedDate());
    }

    @Test
    @DisplayName("댓글 엔티티 생성 테스트")
    void of() {
        // given
        CommentCreateRequest request = mock(CommentCreateRequest.class);
        when(request.content()).thenReturn("댓글 내용 테스트");
        User user = mock(User.class);
        Task task = mock(Task.class);

        // when
        Comment comment = Comment.of(request, user, task);

        // then
        assertThat(comment.getContent()).isEqualTo("댓글 내용 테스트");
        assertThat(comment.getWriter()).isEqualTo(user);
        assertThat(comment.getTask()).isEqualTo(task);
        assertThat(comment.getCreatedDate()).isNotNull();
        assertThat(comment.getModifiedDate()).isNotNull();
    }
}