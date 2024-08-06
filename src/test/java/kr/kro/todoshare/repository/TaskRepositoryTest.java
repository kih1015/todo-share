package kr.kro.todoshare.repository;

import kr.kro.todoshare.domain.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    @DisplayName("모든 작업 조회 테스트")
    void findAll() {
        // given
        Task task1 = Task.builder()
                .title("작업 1")
                .content("작업 내용 1")
                .completed(false)
                .deadline(LocalDateTime.MIN.plusDays(1))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        Task task2 = Task.builder()
                .title("작업 2")
                .content("작업 내용 2")
                .completed(false)
                .deadline(LocalDateTime.MIN.plusDays(2))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        taskRepository.save(task1);
        taskRepository.save(task2);

        // when
        List<Task> tasks = taskRepository.findAll();

        // then
        assertThat(tasks).isNotEmpty();
        assertThat(tasks).hasSize(2);
        assertThat(tasks).extracting("title").containsExactlyInAnyOrder("작업 1", "작업 2");
    }

    @Test
    @DisplayName("작업 ID로 조회 테스트")
    void findById() {
        // given
        Task task = Task.builder()
                .title("작업")
                .content("작업 내용")
                .completed(false)
                .deadline(LocalDateTime.MIN.plusDays(1))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        Task savedTask = taskRepository.save(task);

        // when
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());

        // then
        assertThat(foundTask).isPresent();
        assertThat(foundTask.get().getId()).isEqualTo(savedTask.getId());
        assertThat(foundTask.get().getTitle()).isEqualTo("작업");
        assertThat(foundTask.get().getContent()).isEqualTo("작업 내용");
        assertThat(foundTask.get().getDeadline()).isEqualTo(LocalDateTime.MIN.plusDays(1));
    }

    @Test
    @DisplayName("작업 저장 테스트")
    void save() {
        // given
        Task task = Task.builder()
                .title("새로운 작업")
                .content("새로운 작업 내용")
                .completed(false)
                .deadline(LocalDateTime.MIN.plusDays(3))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();

        // when
        Task savedTask = taskRepository.save(task);

        // then
        assertThat(savedTask).isNotNull();
        assertThat(savedTask.getId()).isNotNull();
        assertThat(savedTask.getTitle()).isEqualTo("새로운 작업");
        assertThat(savedTask.getContent()).isEqualTo("새로운 작업 내용");
    }

    @Test
    @DisplayName("작업 삭제 테스트")
    void deleteById() {
        // given
        Task task = Task.builder()
                .title("삭제할 작업")
                .content("삭제할 작업 내용")
                .completed(false)
                .deadline(LocalDateTime.MIN.plusDays(2))
                .createdDate(LocalDateTime.MIN)
                .modifiedDate(LocalDateTime.MIN)
                .build();
        Task savedTask = taskRepository.save(task);

        // when
        taskRepository.deleteById(savedTask.getId());

        // then
        Optional<Task> foundTask = taskRepository.findById(savedTask.getId());
        assertThat(foundTask).isNotPresent();
    }
}
