package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.TaskCompletedUpdateRequest;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.TaskUpdateRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    private User user;
    private Task task;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .loginId("user1")
                .password("password")
                .nickname("유저1")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();

        task = Task.builder()
                .id(1L)
                .title("제목")
                .content("내용")
                .completed(false)
                .deadline(LocalDateTime.MIN)
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("할일 생성 테스트")
    void create() {
        // given
        TaskCreateRequest request = new TaskCreateRequest("제목", "내용", LocalDateTime.MIN);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.save(any(Task.class))).thenReturn(task);

        // when
        TaskResponse response = taskService.create(request, 1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("할일 ID로 찾기 테스트")
    void getById() {
        // given
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        TaskResponse response = taskService.getById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("모든 할일 찾기 테스트")
    void getAll() {
        // given
        when(taskRepository.findAll()).thenReturn(List.of(task));

        // when
        List<TaskResponse> responses = taskService.getAll();

        // then
        assertThat(responses).isNotEmpty();
        assertThat(responses.get(0).id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("할일 업데이트 테스트")
    void update() {
        // given
        TaskUpdateRequest request = new TaskUpdateRequest("새 제목", "새 내용", LocalDateTime.MIN, true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        TaskResponse response = taskService.update(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.title()).isEqualTo("새 제목");
        assertThat(response.content()).isEqualTo("새 내용");
    }

    @Test
    @DisplayName("할일 완료 상태 업데이트 테스트")
    void testUpdate() {
        // given
        TaskCompletedUpdateRequest request = new TaskCompletedUpdateRequest(true);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));

        // when
        TaskResponse response = taskService.update(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.completed()).isTrue();
    }

    @Test
    @DisplayName("할일 삭제 테스트")
    void delete() {
        // given
        doNothing().when(taskRepository).deleteById(1L);

        // when
        taskService.delete(1L);

        // then
        verify(taskRepository).deleteById(1L);
    }
}
