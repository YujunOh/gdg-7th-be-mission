package gdg.hongik.mission.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 클라이언트에 돌려주는 에러의 종류. 상태 코드와 기본 메시지를 한곳에서 관리한다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	INVALID_INPUT(HttpStatus.BAD_REQUEST, "요청 값을 확인해주세요."),
	PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
	DUPLICATE_PRODUCT_NAME(HttpStatus.CONFLICT, "이미 존재하는 상품명입니다."),
	OUT_OF_STOCK(HttpStatus.CONFLICT, "재고가 부족합니다."),
	INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다.");

	private final HttpStatus status;
	private final String message;
}
