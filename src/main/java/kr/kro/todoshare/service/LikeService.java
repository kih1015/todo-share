package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.repository.LikeRepository;
import kr.kro.todoshare.repository.TaskRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    @Transactional
    public void create(LikeCreateRequest likeCreateRequest) {
        Like like = Like.builder()
                .user(userRepository.findById(likeCreateRequest.user()))
                .task(taskRepository.findById(likeCreateRequest.task()))
                .build();
        likeRepository.save(like);
    }

    @Transactional
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }
}
