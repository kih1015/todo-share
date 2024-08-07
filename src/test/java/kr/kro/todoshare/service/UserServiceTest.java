package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.exception.ConflictException;
import kr.kro.todoshare.exception.LoginFailException;
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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User.builder()
                .id(1L)
                .loginId("user1")
                .password("password")
                .nickname("유저1")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .tasks(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }

    @Test
    @DisplayName("유저 생성 테스트")
    void create() {
        // given
        UserCreateRequest request = new UserCreateRequest("user1", "password", "유저1");
        when(userRepository.findAll()).thenReturn(List.of());
        when(userRepository.save(any(User.class))).thenReturn(user);

        // when
        UserResponse response = userService.create(request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 ID로 찾기 테스트")
    void getById() {
        // given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserResponse response = userService.getById(1L);

        // then
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("유저 업데이트 테스트")
    void update() {
        // given
        UserUpdateRequest request = new UserUpdateRequest("새 닉네임", "새 비밀번호");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // when
        UserResponse response = userService.update(1L, request);

        // then
        assertThat(response).isNotNull();
        assertThat(response.nickname()).isEqualTo("새 닉네임");
    }

    @Test
    @DisplayName("유저 삭제 테스트")
    void delete() {
        // given
        doNothing().when(userRepository).deleteById(1L);

        // when
        userService.delete(1L);

        // then
        verify(userRepository).deleteById(1L);
    }

    @Test
    @DisplayName("유저 로그인 테스트")
    void login() {
        // given
        UserLoginRequest request = new UserLoginRequest("user1", "password");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));

        // when
        Long userId = userService.login(request);

        // then
        assertThat(userId).isEqualTo(1L);
    }

    @Test
    @DisplayName("로그인 실패 테스트")
    void loginFail() {
        // given
        UserLoginRequest request = new UserLoginRequest("user1", "wrongpassword");
        when(userRepository.findByLoginId("user1")).thenReturn(Optional.of(user));

        // when, then
        assertThrows(LoginFailException.class, () -> userService.login(request));
    }

    @Test
    @DisplayName("중복 로그인 ID 테스트")
    void createDuplicateLoginId() {
        // given
        UserCreateRequest request = new UserCreateRequest("user1", "password", "유저1");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // when, then
        assertThrows(ConflictException.class, () -> userService.create(request));
    }

    @Test
    @DisplayName("중복 닉네임 테스트")
    void createDuplicateNickname() {
        // given
        UserCreateRequest request = new UserCreateRequest("user2", "password", "유저1");
        when(userRepository.findAll()).thenReturn(List.of(user));

        // when, then
        assertThrows(ConflictException.class, () -> userService.create(request));
    }
}
