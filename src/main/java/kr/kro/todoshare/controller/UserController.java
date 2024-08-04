package kr.kro.todoshare.controller;

import jakarta.servlet.http.HttpSession;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
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
    public ResponseEntity<UserResponse> createUser(@RequestBody UserCreateRequest request) {
        UserResponse response = userService.create(request);
        return ResponseEntity.created(URI.create("/users/" + response.id())).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUsers(@PathVariable Long id) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/login")
    public String login(@RequestBody UserLoginRequest request, HttpSession session) {
        session.setAttribute("userId", userService.login(request));
        session.setMaxInactiveInterval(1800);
        return "success";
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(@PathVariable Long id, @RequestBody UserUpdateRequest request) {
        UserResponse response = userService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UserResponse> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
