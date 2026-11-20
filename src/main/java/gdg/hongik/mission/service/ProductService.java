package gdg.hongik.mission.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public List<ProductResponse> getProducts() {
		return findAllProducts();
	}

	public ProductResponse getProduct(String itemName) {
		if (itemName.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요.");
		}
		return ProductResponse.from(findByName(itemName));
	}

	@Transactional
	public PurchaseResponse purchase(List<PurchaseItemRequest> request) {
		if (request == null || request.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "구매할 상품이 없습니다.");
		}

		// 같은 상품이 여러번 들어와도 재고 검사가 맞게 상품별 수량을 합치기
		Map<Long, Integer> counts = new LinkedHashMap<>();
		for (PurchaseItemRequest item : request) {
			if (item == null || item.id() == null || item.count() == null || item.count() < 1) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품 ID와 수량(1 이상)을 확인해주세요.");
			}
			counts.merge(item.id(), item.count(), Integer::sum);
		}

		// 하나라도 문제가 있으면 아무것도 구매하지 않도록 차감하기 전에 전부 검사
		Map<Product, Integer> orderLines = new LinkedHashMap<>();
		counts.forEach((id, count) -> {
			Product product = productRepository.findById(id)
					.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
							"상품을 찾을 수 없습니다. id=" + id));
			orderLines.put(product, count);
		});
		orderLines.forEach((product, count) -> {
			if (!product.hasEnoughStock(count)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "재고가 부족합니다. itemName=" + product.getName());
			}
		});

		int totalPrice = 0;
		List<PurchasedProductResponse> items = new ArrayList<>();
		for (Map.Entry<Product, Integer> line : orderLines.entrySet()) {
			Product product = line.getKey();
			int count = line.getValue();
			int price = product.getPrice() * count;
			product.decreaseStock(count);
			totalPrice += price;
			items.add(new PurchasedProductResponse(product.getName(), count, price));
		}
		return new PurchaseResponse(totalPrice, items);
	}

	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {
		if (request.itemName() == null || request.itemName().isBlank()
				|| request.price() == null || request.price() < 0
				|| request.quantity() == null || request.quantity() < 0
				|| request.category() == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"상품명, 가격(0 이상), 재고(0 이상), 카테고리를 확인해주세요.");
		}
		if (productRepository.findByName(request.itemName()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 상품명입니다.");
		}
		Product product = productRepository.save(
				new Product(request.itemName(), request.price(), request.quantity(), request.category()));
		return ProductResponse.from(product);
	}

	@Transactional
	public ProductResponse addStock(StockUpdateRequest request) {
		if (request.itemName() == null || request.itemName().isBlank()
				|| request.quantity() == null || request.quantity() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명과 추가 수량(1 이상)을 확인해주세요.");
		}
		Product product = findByName(request.itemName());
		product.increaseStock(request.quantity());
		return ProductResponse.from(product);
	}

	@Transactional
	public List<ProductResponse> deleteProduct(ProductDeleteRequest request) {
		if (request.itemName() == null || request.itemName().isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 상품명을 입력해주세요.");
		}
		productRepository.delete(findByName(request.itemName()));
		return findAllProducts();
	}

	private Product findByName(String itemName) {
		return productRepository.findByName(itemName)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
	}

	private List<ProductResponse> findAllProducts() {
		return productRepository.findAll(Sort.by("id")).stream().map(ProductResponse::from).toList();
	}
}
