package gdg.hongik.mission;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * DB 대신 메모리에 상품을 보관하는 저장소
 * 서버를 다시 띄우면 초기화됨
 */
public class ProductStore {

	public static List<Product> products = new ArrayList<>();
	public static Long sequence = 1L;

	public static Optional<Product> findById(Long id) {
		return products.stream()
				.filter(product -> product.getId().equals(id))
				.findFirst();
	}

	public static Optional<Product> findByName(String name) {
		return products.stream()
				.filter(product -> product.getName().equals(name))
				.findFirst();
	}
}
