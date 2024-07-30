package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
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
    public LikeResponse create(LikeCreateRequest likeCreateRequest) {
        Like like = Like.builder()
                .user(userRepository.findById(likeCreateRequest.user()))
                .task(taskRepository.findById(likeCreateRequest.task()))
                .build();
        return LikeResponse.from(likeRepository.save(like));
    }

    public LikeResponse getById(Long id) {
        Like like = likeRepository.findById(id);
        return LikeResponse.from(like);
    }

    @Transactional
    public void delete(Long id) {
        likeRepository.deleteById(id);
    }
}
