package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Comment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class CommentRepositoryTest {

    @Autowired
    private CommentRepository commentRepository;

    @Test
    @DisplayName("댓글 저장 테스트")
    void saveComment() {
        // given
        Comment comment = Comment.builder()
                .content("테스트 댓글")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();

        // when
        Comment savedComment = commentRepository.save(comment);

        // then
        assertThat(savedComment).isNotNull();
        assertThat(savedComment.getId()).isNotNull();
        assertThat(savedComment.getContent()).isEqualTo("테스트 댓글");
        assertThat(savedComment.getCreatedDate()).isEqualTo(LocalDateTime.MIN);
        assertThat(savedComment.getModifiedDate()).isEqualTo(LocalDateTime.MIN);
    }

    @Test
    @DisplayName("댓글 ID로 찾기 테스트")
    void findById() {
        // given
        Comment comment = Comment.builder()
                .content("테스트 댓글")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        Comment savedComment = commentRepository.save(comment);

        // when
        Optional<Comment> foundComment = commentRepository.findById(savedComment.getId());

        // then
        assertThat(foundComment).isPresent();
        assertThat(foundComment.get().getId()).isEqualTo(savedComment.getId());
        assertThat(foundComment.get().getContent()).isEqualTo("테스트 댓글");
        assertThat(foundComment.get().getCreatedDate()).isEqualTo(LocalDateTime.MIN);
        assertThat(foundComment.get().getModifiedDate()).isEqualTo(LocalDateTime.MIN);
    }

    @Test
    @DisplayName("댓글 삭제 테스트")
    void deleteById() {
        // given
        Comment comment = Comment.builder()
                .content("테스트 댓글")
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        Comment savedComment = commentRepository.save(comment);

        // when
        commentRepository.deleteById(savedComment.getId());

        // then
        Optional<Comment> foundComment = commentRepository.findById(savedComment.getId());
        assertThat(foundComment).isNotPresent();
    }
}