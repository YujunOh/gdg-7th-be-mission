package gdg.hongik.mission.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import gdg.hongik.mission.Product;
import gdg.hongik.mission.ProductStore;
import gdg.hongik.mission.dto.ProductCreateRequest;
import gdg.hongik.mission.dto.ProductDeleteRequest;
import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.RemainingProductsResponse;
import gdg.hongik.mission.dto.StockAddRequest;
import gdg.hongik.mission.dto.StockAddResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "관리자 (Admin)", description = "관리자용 상품 등록, 재고 추가, 삭제 API")
@RestController
@RequestMapping("/admin/products")
public class ProductAdminController {

	@Operation(summary = "상품 등록", description = "새 상품을 등록합니다. 같은 이름의 상품이 있으면 409를 반환합니다.")
	@PostMapping
	public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductCreateRequest request) {
		if (request.name() == null || request.name().isBlank()
				|| request.price() == null || request.price() < 0
				|| request.stock() == null || request.stock() < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명, 가격(0 이상), 재고(0 이상)를 확인해주세요.");
		}
		if (ProductStore.findByName(request.name()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 상품명입니다.");
		}
		Product product = new Product(ProductStore.sequence++, request.name(), request.price(), request.stock());
		ProductStore.products.add(product);
		return ResponseEntity.status(HttpStatus.CREATED).body(ProductResponse.from(product));
	}

	@Operation(summary = "재고 추가", description = "기존 상품의 재고를 추가하고 최종 재고 수량을 반환합니다.")
	@PatchMapping("/{productId}/stock")
	public ResponseEntity<StockAddResponse> addStock(@PathVariable Long productId,
			@RequestBody StockAddRequest request) {
		if (request.quantity() == null || request.quantity() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "추가 수량은 1 이상이어야 합니다.");
		}
		Product product = ProductStore.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
		product.increaseStock(request.quantity());
		return ResponseEntity.ok(new StockAddResponse(product.getName(), product.getStock()));
	}

	@Operation(summary = "상품 삭제", description = "요청 body의 ids에 담긴 상품을 삭제하고 남은 상품 목록을 반환합니다. 하나라도 없으면 아무것도 삭제하지 않습니다.")
	@DeleteMapping
	public ResponseEntity<RemainingProductsResponse> deleteProducts(@RequestBody ProductDeleteRequest request) {
		if (request.ids() == null || request.ids().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 상품 ID를 입력해주세요.");
		}
		List<Long> distinctIds = request.ids().stream().distinct().toList();
		// 하나라도 없으면 아무것도 지우지 않게 삭제 전에 전부 확인
		for (Long id : distinctIds) {
			if (ProductStore.findById(id).isEmpty()) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id);
			}
		}
		ProductStore.products.removeIf(product -> distinctIds.contains(product.getId()));
		return ResponseEntity.ok(RemainingProductsResponse.from(ProductStore.products));
	}
}
