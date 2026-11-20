package gdg.hongik.mission.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;
	private int price;
	private int stock;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Category category;

	public Product(String name, int price, int stock, Category category) {
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.category = category;
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
