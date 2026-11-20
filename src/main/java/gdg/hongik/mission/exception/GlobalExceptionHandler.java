package gdg.hongik.mission.exception;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

/**
 * 모든 Controller에서 발생한 예외를 한곳에서 {@link ErrorResponse}로 바꾼다.
 * 잘못된 JSON, 없는 주소 같은 Spring MVC 표준 예외는 부모 클래스가 상태 코드를 정해 준다.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusiness(BusinessException e) {
		ErrorCode errorCode = e.getErrorCode();
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, e.getMessage()));
	}

	// Math.addExact, multiplyExact가 int 범위를 넘으면 던진다. 수량이나 금액이 지나치게 큰 요청이다.
	@ExceptionHandler(ArithmeticException.class)
	public ResponseEntity<Object> handleOverflow(ArithmeticException e) {
		return invalidInput("수량 또는 금액이 처리할 수 있는 범위를 넘었습니다.");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleUnexpected(Exception e) {
		log.error("처리하지 못한 예외", e);
		ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, errorCode.getMessage()));
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return invalidInput(joinMessages(ex.getBindingResult().getFieldErrors().stream()
				.map(error -> error.getDefaultMessage())));
	}

	@Override
	protected ResponseEntity<Object> handleHandlerMethodValidationException(HandlerMethodValidationException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		return invalidInput(joinMessages(ex.getAllErrors().stream()
				.map(MessageSourceResolvable::getDefaultMessage)));
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		if (statusCode.value() == HttpStatus.BAD_REQUEST.value()) {
			return invalidInput(ErrorCode.INVALID_INPUT.getMessage());
		}
		// 404(없는 주소), 405(허용되지 않은 메서드) 등은 HTTP 표준 이름을 그대로 쓴다.
		HttpStatus httpStatus = HttpStatus.valueOf(statusCode.value());
		ErrorResponse response = new ErrorResponse(httpStatus.value(), httpStatus.name(), httpStatus.getReasonPhrase());
		return ResponseEntity.status(statusCode).headers(headers).body(response);
	}

	// 검증 오류의 순서는 실행할때마다 달라질 수 있어서 정렬한 뒤 전부 이어 붙인다.
	private String joinMessages(Stream<String> messages) {
		String joined = messages.filter(Objects::nonNull).distinct().sorted().collect(Collectors.joining(" "));
		return joined.isEmpty() ? ErrorCode.INVALID_INPUT.getMessage() : joined;
	}

	private ResponseEntity<Object> invalidInput(String message) {
		ErrorCode errorCode = ErrorCode.INVALID_INPUT;
		return ResponseEntity.status(errorCode.getStatus()).body(ErrorResponse.of(errorCode, message));
	}
}
