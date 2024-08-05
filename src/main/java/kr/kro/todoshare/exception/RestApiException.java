package kr.kro.todoshare.exception;

import kr.kro.todoshare.exception.error.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class RestApiException extends RuntimeException {

    private final ErrorCode errorCode;
}
