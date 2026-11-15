package gdg.hongik.mission.dto.response;

import java.util.List;

public record PurchaseResponse(int totalPrice, List<PurchasedProductResponse> items) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class PurchaseResponse {

	private final int totalPrice;
	private final List<PurchasedProductResponse> items;

	public PurchaseResponse(int totalPrice, List<PurchasedProductResponse> items) {
		this.totalPrice = totalPrice;
		this.items = items;
	}
}
*/
