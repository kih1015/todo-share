package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping()
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse response = userService.create(request);
        return ResponseEntity.created(URI.create("/users/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    ) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        UserResponse response = userService.getById(userId);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public String login(
            @Valid @RequestBody UserLoginRequest request,
            HttpSession session
    ) {
        session.setAttribute("userId", userService.login(request));
        session.setMaxInactiveInterval(1800);
        return "로그인 성공!";
    }

    @PostMapping("/logout")
    public String logout(
            HttpSession session
    ) {
        session.invalidate();
        return "로그아웃 성공!";
    }

    @PutMapping()
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        UserResponse response = userService.update(userId, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping()
    public ResponseEntity<UserResponse> deleteUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }
}
