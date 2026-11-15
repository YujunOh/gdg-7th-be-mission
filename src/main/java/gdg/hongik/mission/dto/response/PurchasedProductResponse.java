package gdg.hongik.mission.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PurchasedProductResponse(
		@Schema(description = "상품명", example = "원피스") String itemName,
		@Schema(description = "구매 수량", example = "2") int count,
		@Schema(description = "단가 x 구매 수량", example = "2000") int price) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class PurchasedProductResponse {

	private final String itemName;
	private final int count;
	private final int price;

	public PurchasedProductResponse(String itemName, int count, int price) {
		this.itemName = itemName;
		this.count = count;
		this.price = price;
	}
}
*/
