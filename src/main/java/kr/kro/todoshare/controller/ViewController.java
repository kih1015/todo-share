package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.TaskCreateRequest;
import kr.kro.todoshare.controller.dto.request.UserLoginRequest;
import kr.kro.todoshare.controller.dto.response.TaskResponse;
import kr.kro.todoshare.exception.AuthenticationException;
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

    @GetMapping("/mypage")
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
            @SessionAttribute(name = "userId", required = false) Long userId,
            BindingResult result
    ) {
        if (result.hasErrors()) {
            return "create";
        }
        if (userId == null) {
            return "redirect:/login";
        }
        taskService.create(userId, request);
        return "redirect:/mypage";
    }

    @GetMapping("/task/{id}")
    public String task(@PathVariable Long id, Model model, @SessionAttribute(required = false) Long userId) {
        if (userId == null) {
            model.addAttribute("user", null);
        } else {
            model.addAttribute("user", userService.getById(userId));
        }
        model.addAttribute("task", taskService.getById(id));
        return "task";
    }

    @GetMapping("/login")
    public String showLoginForm(Model model) {
        model.addAttribute("userLoginRequest", new UserLoginRequest("", ""));
        return "login";
    }

    @PostMapping("/login")
    public String login(
            @Valid UserLoginRequest request,
            BindingResult result,
            HttpSession session
    ) {
        if (result.hasErrors()) {
            return "login";
        }
        session.setAttribute("userId", userService.login(request));
        session.setMaxInactiveInterval(1800);
        return "redirect:/mypage"; // Redirect to the home page upon successful login
    }

    @PostMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}
