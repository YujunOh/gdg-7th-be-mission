package gdg.hongik.mission.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gdg.hongik.mission.Product;
import gdg.hongik.mission.ProductStore;
import gdg.hongik.mission.dto.OrderRequest;
import gdg.hongik.mission.dto.OrderResponse;
import gdg.hongik.mission.dto.ProductResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "소비자 (User)", description = "소비자용 상품 조회, 구매 API")
@RestController
public class ProductUserController {

	@Operation(summary = "상품 조회", description = "상품명으로 상품 한 개를 조회합니다. 응답의 productId를 구매에 사용합니다.")
	@GetMapping("/products")
	public ResponseEntity<ProductResponse> getProduct(@RequestParam String name) {
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요.");
		}
		Product product = ProductStore.findByName(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
		return ResponseEntity.ok(ProductResponse.from(product));
	}

	@Operation(summary = "상품 구매", description = "하나 이상의 상품을 한 번에 구매합니다. 하나라도 없거나 재고가 부족하면 전체가 구매되지 않습니다.")
	@PostMapping("/orders")
	public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
		if (request.items() == null || request.items().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "구매할 상품이 없습니다.");
		}

		// 같은 상품이 여러번 들어와도 재고 검사가 맞게 상품별 수량을 합치기
		Map<Long, Integer> quantities = new LinkedHashMap<>();
		for (OrderRequest.Item item : request.items()) {
			if (item == null || item.productId() == null || item.quantity() == null || item.quantity() < 1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 ID와 수량(1 이상)을 확인해주세요.");
			}
			quantities.merge(item.productId(), item.quantity(), Integer::sum);
		}

		// 하나라도 문제가 있으면 아무것도 구매하지 않도록 차감하기 전에 전부 검사
		Map<Product, Integer> orderLines = new LinkedHashMap<>();
		quantities.forEach((productId, quantity) -> {
			Product product = ProductStore.findById(productId)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"상품을 찾을 수 없습니다. id=" + productId));
			orderLines.put(product, quantity);
		});
		orderLines.forEach((product, quantity) -> {
			if (!product.hasEnoughStock(quantity)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "재고가 부족합니다. name=" + product.getName());
			}
		});

		int totalPrice = 0;
		List<OrderResponse.Item> items = new ArrayList<>();
		for (Map.Entry<Product, Integer> line : orderLines.entrySet()) {
			Product product = line.getKey();
			int quantity = line.getValue();
			int amount = product.getPrice() * quantity;
			product.decreaseStock(quantity);
			totalPrice += amount;
			items.add(new OrderResponse.Item(product.getName(), quantity, amount));
		}
		return ResponseEntity.status(HttpStatus.CREATED).body(new OrderResponse(totalPrice, items));
	}
}
