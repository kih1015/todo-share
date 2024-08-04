package kr.kro.todoshare.domain;

import jakarta.persistence.*;
import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "like")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user")
    private User user;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    public static Like of(User user, Task task) {
        return Like.builder()
                .user(user)
                .task(task)
                .build();
    }
}
