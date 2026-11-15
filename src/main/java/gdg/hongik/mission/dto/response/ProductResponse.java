package gdg.hongik.mission.dto.response;

import gdg.hongik.mission.entity.Category;
import gdg.hongik.mission.entity.Product;

public record ProductResponse(Long id, String itemName, int price, int quantity, Category category) {

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(),
				product.getCategory());
	}
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class ProductResponse {

	private final Long id;
	private final String itemName;
	private final int price;
	private final int quantity;
	private final Category category;

	public ProductResponse(Long id, String itemName, int price, int quantity, Category category) {
		this.id = id;
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
		this.category = category;
	}

	public static ProductResponse from(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice(), product.getStock(),
				product.getCategory());
	}
}
*/
