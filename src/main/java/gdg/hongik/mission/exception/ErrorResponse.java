package gdg.hongik.mission.exception;

/**
 * 모든 에러 응답의 공통 형태.
 *
 * @param status  HTTP 상태 코드
 * @param code    에러 종류 ({@link ErrorCode}의 이름)
 * @param message 사람이 읽는 설명
 */
public record ErrorResponse(int status, String code, String message) {

	public static ErrorResponse of(ErrorCode errorCode, String message) {
		return new ErrorResponse(errorCode.getStatus().value(), errorCode.name(), message);
	}
}
