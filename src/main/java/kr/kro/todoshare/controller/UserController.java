package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.UserCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.request.UserUpdateRequest;
import kr.kro.todoshare.controller.dto.response.UserResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "User API", description = "사용자 관련 api")
@RestController
@RequestMapping(value = "/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "회원가입", description = "사용자 회원가입을 진행합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "loginId": "아이디를 작성해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "409", description = "아이디 또는 닉네임 중복", content = @Content(examples = @ExampleObject(value = "로그인 ID가 중복됩니다.")))
    })
    @PostMapping()
    public ResponseEntity<UserResponse> createUser(
            @Valid @RequestBody UserCreateRequest request
    ) {
        UserResponse response = userService.create(request);
        return ResponseEntity.created(URI.create("/users/" + response.id())).body(response);
    }

    @Operation(summary = "회원 정보 불러오기", description = "회원 id로 회원 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원이 존재하지 않음", content = @Content(examples = @ExampleObject(value = "해당 자원이 존재하지 않습니다.")))
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(
            @PathVariable Long id
    ) {
        UserResponse response = userService.getById(id);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "내 정보 불러오기", description = "로그인 한 사용자의 정보를 불러옵니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "내 정보 불러오기 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다.")))
    })
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        checkLogin(userId);
        UserResponse response = userService.getById(userId);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "로그인 요청", description = "서버에 로그인을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그인 성공", content = @Content(examples = @ExampleObject(value = "로그인 성공!"))),
            @ApiResponse(responseCode = "400", description = "로그인 실패", content = @Content(examples = @ExampleObject(value = "아이디 또는 패스워드가 틀립니다.")))
    })
    @PostMapping("/login")
    public String login(
            @Valid @RequestBody UserLoginRequest request,
            HttpSession session
    ) {
        session.setAttribute("userId", userService.login(request));
        session.setMaxInactiveInterval(1800);
        return "로그인 성공!";
    }

    @Operation(summary = "로그아웃 요청", description = "서버에 로그아웃을 요청합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "로그아웃 성공", content = @Content(examples = @ExampleObject(value = "로그아웃 성공!")))
    })
    @PostMapping("/logout")
    public String logout(
            HttpSession session
    ) {
        session.invalidate();
        return "로그아웃 성공!";
    }

    @Operation(summary = "회원 정보 변경", description = "회원의 닉네임과 비밀번호를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "회원 정보 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 인자 기입", content = @Content(examples = @ExampleObject(value = """
                    {
                      "password": "비밀번호를 작성해야 합니다."
                    }
                    """))),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다.")))
    })
    @PutMapping()
    public ResponseEntity<UserResponse> updateUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId,
            @Valid @RequestBody UserUpdateRequest request
    ) {
        checkLogin(userId);
        UserResponse response = userService.update(userId, request);
        return ResponseEntity.ok().body(response);
    }

    @Operation(summary = "회원 탈퇴", description = "회원 정보를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "회원 탈퇴 성공"),
            @ApiResponse(responseCode = "401", description = "로그인 하지 않음", content = @Content(examples = @ExampleObject(value = "로그인이 필요합니다.")))
    })
    @DeleteMapping()
    public ResponseEntity<UserResponse> deleteUser(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        userService.delete(userId);
        return ResponseEntity.noContent().build();
    }

    private static void checkLogin(Long userId) {
        if (userId == null) {
            throw new AuthenticationException();
        }
    }
}
