package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
import kr.kro.todoshare.domain.Like;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.exception.ConflictException;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.mapper.LikeMapper;
import kr.kro.todoshare.repository.LikeRepository;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor()
public class LikeService {
    
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final LikeMapper likeMapper;

    @Transactional
    public LikeResponse create(Long userId, LikeCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(ResourceNotFoundException::new);
        if (user.getLikes().stream().anyMatch(like -> like.getTask().getId().equals(request.task()))) {
            throw new ConflictException("이미 좋아요를 눌렀습니다.");
        }
        Like like = likeMapper.toEntity(userId, request);
        return likeMapper.toResponse(likeRepository.save(like));
    }
}
