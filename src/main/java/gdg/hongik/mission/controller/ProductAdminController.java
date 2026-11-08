package gdg.hongik.mission.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gdg.hongik.mission.dto.ProductCreateRequest;
import gdg.hongik.mission.dto.ProductDeleteRequest;
import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.RemainingProductsResponse;
import gdg.hongik.mission.dto.StockAddRequest;
import gdg.hongik.mission.dto.StockAddResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자 (Admin)", description = "관리자용 상품 등록, 재고 추가, 삭제 API")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

	private final ProductService productService;

	@Operation(summary = "상품 등록", description = "새 상품을 등록합니다. 같은 이름의 상품이 있으면 409를 반환합니다.")
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@Operation(summary = "재고 추가", description = "기존 상품의 재고를 추가하고 최종 재고 수량을 반환합니다.")
	@PatchMapping("/{productId}/stock")
	public ResponseEntity<StockAddResponse> addStock(@PathVariable Long productId,
			@RequestBody StockAddRequest request) {
		return ResponseEntity.ok(productService.addStock(productId, request));
	}

	@Operation(summary = "상품 삭제", description = "요청 body의 ids에 담긴 상품을 삭제하고 남은 상품 목록을 반환합니다. 하나라도 없으면 아무것도 삭제하지 않습니다.")
	@DeleteMapping
	public ResponseEntity<RemainingProductsResponse> deleteProducts(@RequestBody ProductDeleteRequest request) {
		return ResponseEntity.ok(productService.deleteProducts(request));
	}
}
