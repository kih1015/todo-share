package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        User user = User.from(request);
        return UserResponse.from(userRepository.save(user));
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        return UserResponse.from(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id).orElseThrow(ResourceNotFoundException::new);
        user.update(request.nickname(), request.password());
        return UserResponse.from(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }

    public Long login(UserLoginRequest request) {
        User user = userRepository.findByLoginId(request.loginId()).orElseThrow();
        if (!request.password().equals(user.getPassword())) {
            throw new RuntimeException();
        }
        return user.getId();
    }
}
