package kr.kro.todoshare.domain;

import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TaskTest {

    @Test
    void update() {
        // given
        User user = mock(User.class);
        Task task = Task.builder()
                .title("제목 테스트")
                .content("내용 테스트")
                .deadline(LocalDateTime.MAX)
                .completed(false)
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .build();

        String newTitle = "새로운 제목";
        String newContent = "새로운 내용";
        LocalDateTime newDeadline = LocalDateTime.of(2025, 12, 31, 23, 59);
        Boolean newCompleted = true;

        // when
        task.update(newTitle, newContent, newDeadline, newCompleted);

        // then
        assertThat(task.getTitle()).isEqualTo(newTitle);
        assertThat(task.getContent()).isEqualTo(newContent);
        assertThat(task.getDeadline()).isEqualTo(newDeadline);
        assertThat(task.getCompleted()).isEqualTo(newCompleted);
        assertThat(task.getModifiedDate()).isAfter(task.getCreatedDate());
    }

    @Test
    void updateCompleted() {
        // given
        User user = mock(User.class);
        Task task = Task.builder()
                .title("제목 테스트")
                .content("내용 테스트")
                .deadline(LocalDateTime.MAX)
                .completed(false)
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .build();

        // when
        task.update(true);

        // then
        assertThat(task.getCompleted()).isEqualTo(true);
        assertThat(task.getModifiedDate()).isAfter(task.getCreatedDate());
    }

    @Test
    void of() {
        // given
        User user = mock(User.class);
        TaskCreateRequest request = mock(TaskCreateRequest.class);
        when(request.title()).thenReturn("제목 테스트");
        when(request.content()).thenReturn("내용 테스트");
        when(request.deadline()).thenReturn(LocalDateTime.MAX);

        // when
        Task task = Task.of(request, user);

        // then
        assertThat(task.getTitle()).isEqualTo("제목 테스트");
        assertThat(task.getContent()).isEqualTo("내용 테스트");
        assertThat(task.getDeadline()).isEqualTo(LocalDateTime.MAX);
        assertThat(task.getCompleted()).isFalse();
        assertThat(task.getCreatedDate()).isNotNull();
        assertThat(task.getModifiedDate()).isNotNull();
        assertThat(task.getWriter()).isEqualTo(user);
        assertThat(task.getComments()).isNotNull();
        assertThat(task.getLikes()).isNotNull();
    }
}