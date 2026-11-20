package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record StockUpdateRequest(
		@Schema(description = "상품명", example = "원피스")
		@NotBlank(message = "상품명을 입력해주세요.") String itemName,
		@Schema(description = "추가할 수량 (1 이상)", example = "5")
		@NotNull(message = "추가 수량을 입력해주세요.") @Positive(message = "추가 수량은 1 이상이어야 합니다.") Integer quantity) {
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
