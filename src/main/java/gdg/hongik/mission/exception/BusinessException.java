package gdg.hongik.mission.exception;

import lombok.Getter;

/**
 * 비즈니스 규칙을 어겼을 때 던지는 예외의 부모 클래스.
 * 언체크 예외라서 {@code @Transactional} 메서드 안에서 던지면 롤백된다.
 */
@Getter
public class BusinessException extends RuntimeException {

	private final ErrorCode errorCode;

	public BusinessException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}

	public BusinessException(ErrorCode errorCode, String message) {
		super(message);
		this.errorCode = errorCode;
	}
}
