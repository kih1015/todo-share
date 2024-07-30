package kr.kro.todoshare.controller;

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
    public ResponseEntity<LikeResponse> createLike(@RequestBody LikeCreateRequest request) {
        LikeResponse response = likeService.create(request);
        return ResponseEntity.created(URI.create("/likes/" + response.id())).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LikeResponse> deleteLike(@PathVariable Long id) {
        likeService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
