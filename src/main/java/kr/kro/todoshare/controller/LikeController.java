package kr.kro.todoshare.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.LikeCreateRequest;
import kr.kro.todoshare.controller.dto.response.LikeResponse;
import kr.kro.todoshare.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/likes")
@AllArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping()
    public ResponseEntity<LikeResponse> createLike(@Valid @RequestBody LikeCreateRequest request, HttpSession session) {
        LikeResponse response = likeService.create(request, (Long) session.getAttribute("userId"));
        return ResponseEntity.created(URI.create("/likes/" + response.id())).body(response);
    }
}
