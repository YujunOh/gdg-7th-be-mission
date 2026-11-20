package gdg.hongik.mission.dto.request;

import gdg.hongik.mission.entity.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record ProductCreateRequest(
		@Schema(description = "상품명", example = "원피스")
		@NotBlank(message = "상품명을 입력해주세요.") @Size(max = 255, message = "상품명은 255자 이하여야 합니다.") String itemName,
		@Schema(description = "개당 가격 (0 이상)", example = "1000")
		@NotNull(message = "가격을 입력해주세요.") @PositiveOrZero(message = "가격은 0 이상이어야 합니다.") Integer price,
		@Schema(description = "초기 재고 수량 (0 이상)", example = "13")
		@NotNull(message = "재고 수량을 입력해주세요.") @PositiveOrZero(message = "재고 수량은 0 이상이어야 합니다.") Integer quantity,
		@Schema(description = "카테고리 (의류, 전자기기, 화장품, 식품)", example = "의류")
		@NotNull(message = "카테고리를 입력해주세요.") Category category) {
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
