package gdg.hongik.mission.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderRequest(
		@Schema(description = "구매할 상품 목록 (1개 이상)") List<Item> items) {

	@Schema(name = "OrderItemRequest")
	public record Item(
			@Schema(description = "상품 ID", example = "1") Long productId,
			@Schema(description = "구매 수량 (1 이상)", example = "2") Integer quantity) {
	}
}

// 강의식 (Lombok+생성자)
/*
import lombok.Getter;

@Getter
public class OrderRequest {

	private final List<Item> items;

	public OrderRequest(List<Item> items) {
		this.items = items;
	}

	@Getter
	public static class Item {

		private final Long productId;
		private final Integer quantity;

		public Item(Long productId, Integer quantity) {
			this.productId = productId;
			this.quantity = quantity;
		}
	}
}
*/
