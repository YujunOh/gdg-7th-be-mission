package gdg.hongik.mission.dto;

import java.util.List;

import gdg.hongik.mission.entity.Product;
import io.swagger.v3.oas.annotations.media.Schema;

public record RemainingProductsResponse(List<Item> products) {

	@Schema(name = "RemainingProductItem")
	public record Item(String name, int stock) {
	}

	public static RemainingProductsResponse from(List<Product> products) {
		return new RemainingProductsResponse(
				products.stream().map(p -> new Item(p.getName(), p.getStock())).toList());
	}
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class RemainingProductsResponse {

	private final List<Item> products;

	public RemainingProductsResponse(List<Item> products) {
		this.products = products;
	}

	@Getter
	public static class Item {

		private final String name;
		private final int stock;

		public Item(String name, int stock) {
			this.name = name;
			this.stock = stock;
		}
	}

	public static RemainingProductsResponse from(List<Product> products) {
		return new RemainingProductsResponse(
				products.stream().map(p -> new Item(p.getName(), p.getStock())).toList());
	}
}
*/
