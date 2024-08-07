package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.exception.ConflictException;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.repository.LikeRepository;
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
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

    @Mock
    private LikeRepository likeRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private LikeService likeService;

    private User user;
    private Task task;
    private Like like;

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
                .build();

        like = Like.builder()
                .id(1L)
                .user(user)
                .task(task)
                .build();
    }

    @Test
    @DisplayName("좋아요 생성 테스트")
    void create() {
        // given
        LikeCreateRequest request = new LikeCreateRequest(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(likeRepository.findAllByTask(task)).thenReturn(Collections.emptyList());
        when(likeRepository.save(any())).thenReturn(like);

        // when
        LikeResponse response = likeService.create(request, 1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("좋아요 중복 생성 예외 테스트")
    void createDuplicateLike() {
        // given
        LikeCreateRequest request = new LikeCreateRequest(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(likeRepository.findAllByTask(task)).thenReturn(Collections.singletonList(like));

        // when, then
        assertThrows(ConflictException.class, () -> likeService.create(request, 1L));
    }

    @Test
    @DisplayName("좋아요 생성 시 사용자 찾을 수 없는 경우 예외 테스트")
    void createUserNotFound() {
        // given
        LikeCreateRequest request = new LikeCreateRequest(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> likeService.create(request, 1L));
    }

    @Test
    @DisplayName("좋아요 생성 시 할일 찾을 수 없는 경우 예외 테스트")
    void createTaskNotFound() {
        // given
        LikeCreateRequest request = new LikeCreateRequest(1L);
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        // when, then
        assertThrows(ResourceNotFoundException.class, () -> likeService.create(request, 1L));
    }
}
