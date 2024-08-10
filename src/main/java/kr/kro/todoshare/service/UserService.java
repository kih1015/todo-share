package kr.kro.todoshare.service;

import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.domain.User;
import kr.kro.todoshare.exception.ConflictException;
import kr.kro.todoshare.exception.LoginFailException;
import kr.kro.todoshare.exception.ResourceNotFoundException;
import kr.kro.todoshare.repository.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor()
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public UserResponse create(UserCreateRequest request) {
        if (userRepository.findAll().stream().anyMatch(user -> user.getLoginId().equals(request.loginId()))) {
            throw new ConflictException("로그인 ID가 중복됩니다.");
        }
        if (userRepository.findAll().stream().anyMatch(user -> user.getNickname().equals(request.nickname()))) {
            throw new ConflictException("닉네임이 중복됩니다.");
        }
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
        User user = userRepository.findByLoginId(request.loginId()).orElseThrow(LoginFailException::new);
        if (!request.password().equals(user.getPassword())) {
            throw new LoginFailException();
        }
        return user.getId();
    }
}
