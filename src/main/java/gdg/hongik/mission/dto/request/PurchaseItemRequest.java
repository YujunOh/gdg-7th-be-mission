package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PurchaseItemRequest(
		@Schema(description = "상품 ID", example = "1")
		@NotNull(message = "상품 ID를 입력해주세요.") Long id,
		@Schema(description = "구매 수량 (1 이상)", example = "2")
		@NotNull(message = "구매 수량을 입력해주세요.") @Positive(message = "구매 수량은 1 이상이어야 합니다.") Integer count) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class PurchaseItemRequest {

	private final Long id;
	private final Integer count;

	public PurchaseItemRequest(Long id, Integer count) {
		this.id = id;
		this.count = count;
	}
}
*/
