package kr.kro.todoshare.domain;

import jakarta.persistence.*;
import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "comment")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne
    @JoinColumn(name = "writer")
    private User writer;

    @ManyToOne
    @JoinColumn(name = "task")
    private Task task;

    public void update(String content) {
        this.content = content;
        this.modifiedDate = LocalDateTime.now();
    }

    public static Comment of(CommentCreateRequest request, User writer, Task task) {
        return Comment.builder()
                .content(request.content())
                .writer(writer)
                .task(task)
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .build();
    }
}
