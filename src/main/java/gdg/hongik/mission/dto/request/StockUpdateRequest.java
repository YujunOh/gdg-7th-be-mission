package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record StockUpdateRequest(
		@Schema(description = "상품명", example = "원피스") String itemName,
		@Schema(description = "추가할 수량 (1 이상)", example = "5") Integer quantity) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class StockUpdateRequest {

	private final String itemName;
	private final Integer quantity;

	public StockUpdateRequest(String itemName, Integer quantity) {
		this.itemName = itemName;
		this.quantity = quantity;
	}
}
*/
