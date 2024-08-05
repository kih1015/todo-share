package kr.kro.todoshare.exception;

public record ErrorResponse(
        String status,
        String message
) {
}
