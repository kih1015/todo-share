package kr.kro.todoshare.domain;

import jakarta.persistence.*;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "user")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id")
    private String loginId;

    @Column(name = "password")
    private String password;

    @Column(name = "nickname")
    private String nickname;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    public void update(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;
        this.modifiedDate = LocalDateTime.now();
    }

    public static User from(UserCreateRequest request) {
        return User.builder()
                .loginId(request.loginId())
                .password(request.password())
                .nickname(request.nickname())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
