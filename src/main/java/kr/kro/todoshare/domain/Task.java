package kr.kro.todoshare.domain;

import jakarta.persistence.*;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "completed")
    private Boolean completed;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Column(name = "created_date")
    private LocalDateTime createdDate;

    @Column(name = "modified_date")
    private LocalDateTime modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer")
    private User writer;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "task")
    private List<Like> likes = new ArrayList<>();

    public void update(String title, String content, LocalDateTime deadline, Boolean completed) {
        this.title = title;
        this.content = content;
        this.deadline = deadline;
        this.completed = completed;
        this.modifiedDate = LocalDateTime.now();
    }

    public void update(Boolean completed) {
        this.completed = completed;
    }

    public static Task of(TaskCreateRequest request, User writer) {
        return Task.builder()
                .title(request.title())
                .content(request.content())
                .completed(false)
                .deadline(request.deadline())
                .createdDate(LocalDateTime.now())
                .modifiedDate(LocalDateTime.now())
                .writer(writer)
                .comments(new ArrayList<>())
                .likes(new ArrayList<>())
                .build();
    }
}
