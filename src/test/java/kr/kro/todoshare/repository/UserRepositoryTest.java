package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("모든 사용자 조회 테스트")
    void findAll() {
        // given
        User user1 = User.builder()
                .loginId("user1")
                .password("password1")
                .nickname("유저1")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        User user2 = User.builder()
                .loginId("user2")
                .password("password2")
                .nickname("유저2")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        userRepository.save(user1);
        userRepository.save(user2);

        // when
        List<User> users = userRepository.findAll();

        // then
        assertThat(users).isNotEmpty();
        assertThat(users).hasSize(2);
        assertThat(users).extracting("loginId").containsExactlyInAnyOrder("user1", "user2");
    }

    @Test
    @DisplayName("사용자 ID로 조회 테스트")
    void findById() {
        // given
        User user = User.builder()
                .loginId("user")
                .password("password")
                .nickname("유저")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        User savedUser = userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findById(savedUser.getId());

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getId()).isEqualTo(savedUser.getId());
        assertThat(foundUser.get().getLoginId()).isEqualTo("user");
        assertThat(foundUser.get().getNickname()).isEqualTo("유저");
    }

    @Test
    @DisplayName("로그인 ID로 사용자 조회 테스트")
    void findByLoginId() {
        // given
        User user = User.builder()
                .loginId("unique_login_id")
                .password("password")
                .nickname("유저")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        userRepository.save(user);

        // when
        Optional<User> foundUser = userRepository.findByLoginId("unique_login_id");

        // then
        assertThat(foundUser).isPresent();
        assertThat(foundUser.get().getLoginId()).isEqualTo("unique_login_id");
    }

    @Test
    @DisplayName("사용자 저장 테스트")
    void save() {
        // given
        User user = User.builder()
                .loginId("new_user")
                .password("new_password")
                .nickname("새로운 유저")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();

        // when
        User savedUser = userRepository.save(user);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getId()).isNotNull();
        assertThat(savedUser.getLoginId()).isEqualTo("new_user");
        assertThat(savedUser.getNickname()).isEqualTo("새로운 유저");
    }

    @Test
    @DisplayName("사용자 삭제 테스트")
    void deleteById() {
        // given
        User user = User.builder()
                .loginId("delete_user")
                .password("delete_password")
                .nickname("삭제 유저")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        User savedUser = userRepository.save(user);

        // when
        userRepository.deleteById(savedUser.getId());

        // then
        Optional<User> foundUser = userRepository.findById(savedUser.getId());
        assertThat(foundUser).isNotPresent();
    }
}
