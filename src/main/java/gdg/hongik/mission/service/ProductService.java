package gdg.hongik.mission.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import gdg.hongik.mission.dto.request.ProductCreateRequest;
import gdg.hongik.mission.dto.request.ProductDeleteRequest;
import gdg.hongik.mission.dto.request.PurchaseItemRequest;
import gdg.hongik.mission.dto.request.StockUpdateRequest;
import gdg.hongik.mission.dto.response.ProductResponse;
import gdg.hongik.mission.dto.response.PurchaseResponse;
import gdg.hongik.mission.dto.response.PurchasedProductResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.exception.DuplicateProductNameException;
import gdg.hongik.mission.exception.OutOfStockException;
import gdg.hongik.mission.exception.ProductNotFoundException;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

/**
 * 상품 조회, 구매, 등록, 재고 추가, 삭제의 비즈니스 로직.
 * 입력값의 형식 검사는 요청 DTO의 Bean Validation이 맡고, 여기서는 규칙만 다룬다.
 */
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	/**
	 * 등록된 전체 상품을 id 순서로 조회한다.
	 */
	public List<ProductResponse> getProducts() {
		return findAllProducts();
	}

	/**
	 * 상품명이 정확히 일치하는 상품 한 개를 조회한다.
	 *
	 * @throws ProductNotFoundException 해당 이름의 상품이 없을 때
	 */
	public ProductResponse getProduct(String itemName) {
		return ProductResponse.from(findByName(itemName));
	}

	/**
	 * 여러 상품을 한번에 구매한다. 같은 상품이 여러번 들어오면 수량을 합친다.
	 * 하나라도 실패하면 예외가 발생해 트랜잭션이 롤백되므로 아무것도 차감되지 않는다.
	 *
	 * @throws ProductNotFoundException 없는 상품 ID가 있을 때
	 * @throws OutOfStockException      재고가 모자랄 때
	 */
	@Transactional
	public PurchaseResponse purchase(List<PurchaseItemRequest> request) {
		int totalPrice = 0;
		List<PurchasedProductResponse> items = new ArrayList<>();
		for (Map.Entry<Long, Integer> line : mergeCounts(request).entrySet()) {
			Product product = findById(line.getKey());
			int count = line.getValue();
			int price = Math.multiplyExact(product.getPrice(), count);
			product.decreaseStock(count);
			totalPrice = Math.addExact(totalPrice, price);
			items.add(new PurchasedProductResponse(product.getName(), count, price));
		}
		return new PurchaseResponse(totalPrice, items);
	}

	/**
	 * 새 상품을 등록한다.
	 *
	 * @throws DuplicateProductNameException 같은 이름의 상품이 이미 있을 때
	 */
	@Transactional
	public ProductResponse createProduct(ProductCreateRequest request) {
		if (productRepository.findByName(request.itemName()).isPresent()) {
			throw new DuplicateProductNameException();
		}
		Product product = productRepository.save(
				new Product(request.itemName(), request.price(), request.quantity(), request.category()));
		return ProductResponse.from(product);
	}

	/**
	 * 상품명으로 찾은 상품의 재고를 늘린다. save 없이 더티 체킹으로 반영된다.
	 *
	 * @throws ProductNotFoundException 해당 이름의 상품이 없을 때
	 */
	@Transactional
	public ProductResponse addStock(StockUpdateRequest request) {
		Product product = findByName(request.itemName());
		product.increaseStock(request.quantity());
		return ProductResponse.from(product);
	}

	/**
	 * 상품명으로 찾은 상품을 삭제하고 남은 전체 상품을 돌려준다.
	 *
	 * @throws ProductNotFoundException 해당 이름의 상품이 없을 때
	 */
	@Transactional
	public List<ProductResponse> deleteProduct(ProductDeleteRequest request) {
		productRepository.delete(findByName(request.itemName()));
		return findAllProducts();
	}

	private Map<Long, Integer> mergeCounts(List<PurchaseItemRequest> request) {
		Map<Long, Integer> counts = new LinkedHashMap<>();
		for (PurchaseItemRequest item : request) {
			counts.merge(item.id(), item.count(), Math::addExact);
		}
		return counts;
	}

	private Product findById(Long id) {
		return productRepository.findById(id).orElseThrow(() -> new ProductNotFoundException(id));
	}

	private Product findByName(String itemName) {
		return productRepository.findByName(itemName).orElseThrow(ProductNotFoundException::new);
	}

	private List<ProductResponse> findAllProducts() {
		return productRepository.findAll(Sort.by("id")).stream().map(ProductResponse::from).toList();
	}
}
