package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.*;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.CommentService;
import kr.kro.todoshare.service.LikeService;
import kr.kro.todoshare.service.TaskService;
import kr.kro.todoshare.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Controller
public class ViewController {

    private final TaskService taskService;
    private final UserService userService;
    private final CommentService commentService;
    private final LikeService likeService;

    @GetMapping("/")
    public String home(Model model, @SessionAttribute(required = false) Long userId) {
        if (userId == null) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", userService.getById(userId));
        }
        List<TaskResponse> tasks = taskService.getAll();
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @GetMapping("users/me")
    public String mypage(Model model, @SessionAttribute(required = false) Long userId) {
        if (userId == null) {
            throw new AuthenticationException();
        } else {
            model.addAttribute("user", userService.getById(userId));
        }
        return "mypage";
    }

    @GetMapping("/task/create")
    public String create(Model model, @SessionAttribute(required = false) Long userId) {
        if (userId == null) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", userService.getById(userId));
        }
        model.addAttribute("taskCreateRequest", new TaskCreateRequest("","", LocalDateTime.now()));
        return "create";
    }

    @PostMapping("/task/create")
    public String createTask(
            @Valid TaskCreateRequest request,
            @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        if (userId == null) {
            return "redirect:/users/login";
        }
        taskService.create(userId, request);
        return "redirect:/users/me";
    }

    @PostMapping("/task/delete/{id}")
    public String deleteTask(
            @PathVariable Long id,
            @ModelAttribute("task") Long taskId,
            @SessionAttribute(required = false) Long userId
    ) {
        if (!taskService.getById(id).writer().id().equals(userId)) {
            throw new AccessDeniedException();
        }
        taskService.delete(id);
        return "redirect:/users/me";
    }

    @GetMapping("/task/{id}")
    public String task(@PathVariable Long id, Model model, @SessionAttribute(required = false) Long userId) {
        if (userId == null) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", userService.getById(userId));
        }
        model.addAttribute("task", taskService.getById(id));
        model.addAttribute("commentCreateRequest", new CommentCreateRequest("", id));
        return "task";
    }

    @PostMapping("/comment")
    public String createComment(
            @Valid CommentCreateRequest request,
            @SessionAttribute(required = false) Long userId
    ) {
        commentService.create(userId, request);
        return "redirect:/task/" + request.task().toString();
    }

    @PostMapping("/comment/delete/{id}")
    public String deleteComment(
            @PathVariable Long id,
            @ModelAttribute("task") Long taskId,
            @SessionAttribute(required = false) Long userId
    ) {
        if (!commentService.getById(id).writer().id().equals(userId)) {
            throw new AccessDeniedException();
        }
        commentService.delete(id);
        return "redirect:/task/" + taskId.toString();
    }

    @GetMapping("/users/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest("", ""));
        return "login";
    }

    @PostMapping("/users/login")
    public String login(
            @Valid UserLoginRequest request,
            HttpSession session
    ) {
        session.setAttribute("userId", userService.login(request));
        session.setMaxInactiveInterval(1800);
        return "redirect:/users/me";
    }

    @GetMapping("/users/signup")
    public String showSignupForm(Model model) {
        return "signup";
    }

    @PostMapping("/users/signup")
    public String handleSignup(
            @Valid UserCreateRequest request
    ) {
        userService.create(request);
        return "redirect:/users/signup-success";
    }

    @PostMapping("/like")
    public String like(
            @Valid LikeCreateRequest request,
            @SessionAttribute Long userId
            ) {
        likeService.create(userId, request);
        return "redirect:/task/" + request.task().toString();
    }

    @GetMapping("/users/signup-success")
    public String showSignupSuccess() {
        return "signup-success";
    }

    @PostMapping("/users/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/users/delete")
    public String cancel(HttpSession session) {
        if (session.getAttribute("userId") == null) {
            throw new AuthenticationException();
        }
        userService.delete((Long) session.getAttribute("userId"));
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/users/update")
    public String showUpdateUser(
            @SessionAttribute(required = false) Long userId,
            Model model
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        model.addAttribute("user", userService.getById(userId));
        return "user-update";
    }

    @PostMapping("/users/update")
    public String updateUser(
            @Valid UserUpdateRequest request,
            @SessionAttribute Long userId
    ) {
        if (userId == null) {
            throw new AuthenticationException();
        }
        userService.update(userId, request);
        return "redirect:/users/me";
    }
}
