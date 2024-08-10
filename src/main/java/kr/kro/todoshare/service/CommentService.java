package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.request.CommentUpdateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.domain.Comment;
import kr.kro.todoshare.exception.ReferenceException;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.mapper.CommentMapper;
import kr.kro.todoshare.repository.CommentRepository;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor()
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;
    private final CommentMapper commentMapper;

    @Transactional
    public CommentResponse create(Long writerId, CommentCreateRequest request) {
        userRepository.findById(writerId).orElseThrow(() -> new ReferenceException("사용자가 존재하지 않습니다."));
        taskRepository.findById(request.task()).orElseThrow(() -> new ReferenceException("할일이 존재하지 않습니다."));
        Comment comment = commentMapper.toEntity(writerId, request);
        return commentMapper.toResponse(commentRepository.save(comment));
    }

    public CommentResponse getById(Long id) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return commentMapper.toResponse(comment);
    }

    @Transactional
    public CommentResponse update(Long id, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        comment.update(request.content());
        return commentMapper.toResponse(comment);
    }

    @Transactional
    public void delete(Long id) {
        commentRepository.deleteById(id);
    }
}
