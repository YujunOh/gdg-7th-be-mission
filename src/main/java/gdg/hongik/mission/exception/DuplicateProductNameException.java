package gdg.hongik.mission.exception;

public class DuplicateProductNameException extends BusinessException {

	public DuplicateProductNameException() {
		super(ErrorCode.DUPLICATE_PRODUCT_NAME);
	}
}
