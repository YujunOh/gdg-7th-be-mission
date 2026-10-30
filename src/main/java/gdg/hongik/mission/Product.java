package gdg.hongik.mission;

import lombok.Getter;

@Getter
public class Product {

	private final Long id;
	private final String name;
	private final int price;
	private int stock;

	public Product(Long id, String name, int price, int stock) {
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
	}

	public void increaseStock(int quantity) {
		this.stock += quantity;
	}

	public void decreaseStock(int quantity) {
		this.stock -= quantity;
	}

	public boolean hasEnoughStock(int quantity) {
		return stock >= quantity;
	}
}
