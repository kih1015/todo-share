package kr.kro.todoshare.controller;

import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import kr.kro.todoshare.controller.dto.request.CommentCreateRequest;
import kr.kro.todoshare.controller.dto.request.CommentUpdateRequest;
import kr.kro.todoshare.controller.dto.response.CommentResponse;
import kr.kro.todoshare.exception.AccessDeniedException;
import kr.kro.todoshare.exception.AuthenticationException;
import kr.kro.todoshare.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/comments")
@AllArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping()
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        CommentResponse response = commentService.create(request, writerId);
        return ResponseEntity.created(URI.create("/comments/" + response.id())).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long id,
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        if (!commentService.getById(id).writer().equals(writerId)) {
            throw new AccessDeniedException();
        }
        CommentResponse response = commentService.update(id, request);
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CommentResponse> deleteComment(
            @Parameter(hidden = true) @SessionAttribute(name = "userId", required = false) Long writerId,
            @PathVariable Long id
    ) {
        if (writerId == null) {
            throw new AuthenticationException();
        }
        if (!commentService.getById(id).writer().equals(writerId)) {
            throw new AccessDeniedException();
        }
        commentService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
