package gdg.hongik.mission.dto.request;

import gdg.hongik.mission.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCreateRequest(
		@Schema(description = "상품명", example = "원피스") String itemName,
		@Schema(description = "개당 가격 (0 이상)", example = "1000") Integer price,
		@Schema(description = "초기 재고 수량 (0 이상)", example = "13") Integer quantity,
		@Schema(description = "카테고리 (의류, 전자기기, 화장품, 식품)", example = "의류") Category category) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class ProductCreateRequest {

	private final String itemName;
	private final Integer price;
	private final Integer quantity;
	private final Category category;

	public ProductCreateRequest(String itemName, Integer price, Integer quantity, Category category) {
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}
}
*/
