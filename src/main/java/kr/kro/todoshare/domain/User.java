package kr.kro.todoshare.domain;

import jakarta.persistence.*;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "writer")
    private List<Task> tasks = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "writer")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "user")
    private List<Like> likes = new ArrayList<>();

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
                .tasks(new ArrayList<>())
                .likes(new ArrayList<>())
                .comments(new ArrayList<>())
                .build();
    }
}
