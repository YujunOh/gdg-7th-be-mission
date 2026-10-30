package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductCreateRequest(
		@Schema(description = "상품명", example = "갈치") String name,
		@Schema(description = "개당 가격 (0 이상)", example = "15000") Integer price,
		@Schema(description = "초기 재고 수량 (0 이상)", example = "30") Integer stock) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class ProductCreateRequest {

	private final String name;
	private final Integer price;
	private final Integer stock;

	public ProductCreateRequest(String name, Integer price, Integer stock) {
		this.name = name;
		this.price = price;
		this.stock = stock;
	}
}
*/
