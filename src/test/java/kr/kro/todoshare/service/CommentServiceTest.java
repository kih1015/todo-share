package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.request.CommentUpdateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.domain.Task;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.repository.CommentRepository;
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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private CommentService commentService;

    private User user;
    private Task task;
    private Comment comment;

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

        comment = Comment.builder()
                .id(1L)
                .content("댓글 내용")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .writer(user)
                .task(task)
                .build();
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    void create() {
        // given
        CommentCreateRequest request = new CommentCreateRequest("댓글 내용", 1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
        when(commentRepository.save(any())).thenReturn(comment);

        // when
        CommentResponse response = commentService.create(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.content()).isEqualTo("댓글 내용");
    }

    @Test
    @DisplayName("댓글 ID로 조회 테스트")
    void getById() {
        // given
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // when
        CommentResponse response = commentService.getById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.content()).isEqualTo("댓글 내용");
    }

    @Test
    @DisplayName("댓글 업데이트 테스트")
    void update() {
        // given
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글 내용");
        when(commentRepository.findById(1L)).thenReturn(Optional.of(comment));

        // when
        CommentResponse response = commentService.update(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo("수정된 댓글 내용");
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void delete() {
        // given
        doNothing().when(commentRepository).deleteById(1L);

        // when
        commentService.delete(1L);

        // then
        verify(commentRepository).deleteById(1L);
    }
}
