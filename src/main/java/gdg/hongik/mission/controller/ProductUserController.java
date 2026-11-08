package gdg.hongik.mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdg.hongik.mission.dto.OrderRequest;
import gdg.hongik.mission.dto.OrderResponse;
import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "소비자 (User)", description = "소비자용 상품 조회, 구매 API")
@RestController
@RequiredArgsConstructor
public class ProductUserController {

	private final ProductService productService;

	@Operation(summary = "상품 조회", description = "상품명으로 상품 한 개를 조회합니다. 응답의 productId를 구매에 사용합니다.")
	@GetMapping("/products")
	public ResponseEntity<ProductResponse> getProduct(@RequestParam String name) {
		return ResponseEntity.ok(productService.getProduct(name));
	}

	@Operation(summary = "상품 구매", description = "하나 이상의 상품을 한 번에 구매합니다. 하나라도 없거나 재고가 부족하면 전체가 구매되지 않습니다.")
	@PostMapping("/orders")
	public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.order(request));
	}
}
