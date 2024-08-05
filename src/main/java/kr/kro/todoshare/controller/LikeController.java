package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
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
    public ResponseEntity<LikeResponse> createLike(
            @Valid @RequestBody LikeCreateRequest request,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long userId
    ) {
        LikeResponse response = likeService.create(request, userId);
        return ResponseEntity.created(URI.create("/likes/" + response.id())).body(response);
    }
}
