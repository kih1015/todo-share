package kr.kro.todoshare.domain;

import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import org.assertj.core.api.AbstractBigDecimalAssert;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserTest {

    @Test
    void update() {
        // given
        User user = User.builder()
                .loginId("testLoginId")
                .password("oldPassword")
                .nickname("oldNickname")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .tasks(new ArrayList<>())
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();

        String newNickname = "newNickname";
        String newPassword = "newPassword";

        // when
        user.update(newNickname, newPassword);

        // then
        assertThat(user.getNickname()).isEqualTo(newNickname);
        assertThat(user.getPassword()).isEqualTo(newPassword);
        assertThat(user.getModifiedDate()).isNotNull();
        assertThat(user.getModifiedDate()).isAfter(user.getCreatedDate());
    }

    @Test
    void from() {
        // given
        UserCreateRequest request = mock(UserCreateRequest.class);
        when(request.loginId()).thenReturn("testLoginId");
        when(request.password()).thenReturn("testPassword");
        when(request.nickname()).thenReturn("testNickname");

        // when
        User user = User.from(request);

        // then
        assertThat(user.getLoginId()).isEqualTo("testLoginId");
        assertThat(user.getPassword()).isEqualTo("testPassword");
        assertThat(user.getNickname()).isEqualTo("testNickname");
        assertThat(user.getCreatedDate()).isNotNull();
        assertThat(user.getModifiedDate()).isNotNull();
        assertThat(user.getTasks()).isNotNull();
        assertThat(user.getComments()).isNotNull();
        assertThat(user.getLikes()).isNotNull();
    }
}