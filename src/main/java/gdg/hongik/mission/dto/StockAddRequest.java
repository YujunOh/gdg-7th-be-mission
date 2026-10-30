package gdg.hongik.mission.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record StockAddRequest(
		@Schema(description = "추가할 수량 (1 이상)", example = "10") Integer quantity) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class StockAddRequest {

	private final Integer quantity;

	public StockAddRequest(Integer quantity) {
		this.quantity = quantity;
	}
}
*/
