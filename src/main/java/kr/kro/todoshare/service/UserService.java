package kr.kro.todoshare.service;

import jakarta.transaction.Transactional;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@Transactional
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreateRequest userCreateRequest) {
        User user = User.builder().loginId(userCreateRequest.loginId()).password(userCreateRequest.password()).nickname(userCreateRequest.nickname()).build();
        return UserResponse.fromUser(user);
    }

    public UserResponse getById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return UserResponse.fromUser(user);
    }

    @Transactional
    public UserResponse update(Long id, UserUpdateRequest userUpdateRequest) {
        User user = userRepository.findById(id).orElseThrow();
        user.update(userUpdateRequest.nickname(), userUpdateRequest.password());
        return UserResponse.fromUser(user);
    }

    @Transactional
    public void delete(Long id) {
        userRepository.deleteById(id);
    }
}
