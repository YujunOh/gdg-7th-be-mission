package gdg.hongik.mission.exception;

public class ProductNotFoundException extends BusinessException {

	public ProductNotFoundException() {
		super(ErrorCode.PRODUCT_NOT_FOUND);
	}

	public ProductNotFoundException(Long id) {
		super(ErrorCode.PRODUCT_NOT_FOUND, ErrorCode.PRODUCT_NOT_FOUND.getMessage() + " id=" + id);
	}
}
