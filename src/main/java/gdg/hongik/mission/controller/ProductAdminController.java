package gdg.hongik.mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "관리자 (Admin)", description = "관리자용 상품 등록, 재고 추가, 삭제 API")
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/admin/products")
@RequiredArgsConstructor
public class ProductAdminController {

	private final ProductService productService;

	@Operation(summary = "상품 등록", description = "새 상품을 등록합니다. 같은 이름의 상품이 있으면 409를 반환합니다.")
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductCreateRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(request));
	}

	@Operation(summary = "재고 추가", description = "상품명으로 찾은 상품의 재고를 추가하고 변경된 상품 정보를 반환합니다.")
	@PatchMapping("/stock")
	public ResponseEntity<ProductResponse> addStock(@Valid @RequestBody StockUpdateRequest request) {
		return ResponseEntity.ok(productService.addStock(request));
	}

	@Operation(summary = "상품 삭제", description = "상품명으로 찾은 상품을 삭제하고 남은 전체 상품 목록을 반환합니다.")
	@DeleteMapping
	public ResponseEntity<List<ProductResponse>> deleteProduct(@Valid @RequestBody ProductDeleteRequest request) {
		return ResponseEntity.ok(productService.deleteProduct(request));
	}
}
