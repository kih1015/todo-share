package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.request.CommentUpdateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.repository.CommentRepository;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public CommentResponse create(CommentCreateRequest request) {
        Comment comment = Comment.of(request, userRepository.findById(request.writer()), taskRepository.findById(request.task()));
        return CommentResponse.from(commentRepository.save(comment));
    }

    public CommentResponse getById(Long id) {
        Comment comment = commentRepository.findById(id);
        return CommentResponse.from(comment);
    }

    @Transactional
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(id);
        comment.update(request.content());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
