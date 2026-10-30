package gdg.hongik.mission.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record OrderResponse(int totalPrice, List<Item> items) {

	@Schema(name = "OrderItemResponse")
	public record Item(String name, int quantity, int amount) {
	}
}

// 강의식 (Lombok+생성자)
/*
import lombok.Getter;

@Getter
public class OrderResponse {

	private final int totalPrice;
	private final List<Item> items;

	public OrderResponse(int totalPrice, List<Item> items) {
		this.totalPrice = totalPrice;
		this.items = items;
	}

	@Getter
	public static class Item {

		private final String name;
		private final int quantity;
		private final int amount;

		public Item(String name, int quantity, int amount) {
			this.name = name;
			this.quantity = quantity;
			this.amount = amount;
		}
	}
}
*/
