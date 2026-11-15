package gdg.hongik.mission.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "소비자 (User)", description = "소비자용 상품 조회, 구매 API")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductUserController {

	private final ProductService productService;

	@Operation(summary = "전체 상품 목록 조회", description = "등록된 전체 상품을 조회합니다.")
	@GetMapping
	public ResponseEntity<List<ProductResponse>> getProducts() {
		return ResponseEntity.ok(productService.getProducts());
	}

	@Operation(summary = "상품명 단건 검색", description = "상품명으로 상품 한 개를 조회합니다.")
	@GetMapping(params = "itemName")
	public ResponseEntity<ProductResponse> getProduct(@RequestParam String itemName) {
		return ResponseEntity.ok(productService.getProduct(itemName));
	}

	@Operation(summary = "장바구니 상품 일괄 구매", description = "하나 이상의 상품을 한 번에 구매합니다. 하나라도 없거나 재고가 부족하면 전체가 구매되지 않습니다.")
	@PostMapping("/purchase")
	public ResponseEntity<PurchaseResponse> purchase(@RequestBody List<PurchaseItemRequest> request) {
		return ResponseEntity.ok(productService.purchase(request));
	}
}
