package kr.kro.todoshare.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ConflictException extends RuntimeException {
    private String message;
}
