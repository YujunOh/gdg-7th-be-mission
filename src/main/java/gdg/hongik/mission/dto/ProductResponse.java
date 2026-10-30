package gdg.hongik.mission.dto;

import gdg.hongik.mission.Product;

public record ProductResponse(Long productId, String name, int price, int stock) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
	}
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class ProductResponse {

	private final Long productId;
	private final String name;
	private final int price;
	private final int stock;

	public ProductResponse(Long productId, String name, int price, int stock) {
		this.productId = productId;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock());
	}
}
*/
