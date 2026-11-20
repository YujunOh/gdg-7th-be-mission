package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record PurchaseItemRequest(
		@Schema(description = "상품 ID", example = "1") Long id,
		@Schema(description = "구매 수량 (1 이상)", example = "2") Integer count) {
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
