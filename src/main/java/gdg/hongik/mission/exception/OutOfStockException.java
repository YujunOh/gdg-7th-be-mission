package gdg.hongik.mission.exception;

public class OutOfStockException extends BusinessException {

	public OutOfStockException(String itemName) {
		super(ErrorCode.OUT_OF_STOCK, ErrorCode.OUT_OF_STOCK.getMessage() + " itemName=" + itemName);
	}
}
