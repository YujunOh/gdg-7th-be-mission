package gdg.hongik.mission.dto;

public record StockAddResponse(String name, int stock) {
}

// 강의 스타일 (Lombok + 생성자)
/*
import lombok.Getter;

@Getter
public class StockAddResponse {

	private final String name;
	private final int stock;

	public StockAddResponse(String name, int stock) {
		this.name = name;
		this.stock = stock;
	}
}
*/
