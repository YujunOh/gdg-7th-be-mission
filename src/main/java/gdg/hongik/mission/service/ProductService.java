package gdg.hongik.mission.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import gdg.hongik.mission.dto.OrderRequest;
import gdg.hongik.mission.dto.OrderResponse;
import gdg.hongik.mission.dto.ProductCreateRequest;
import gdg.hongik.mission.dto.ProductDeleteRequest;
import gdg.hongik.mission.dto.ProductResponse;
import gdg.hongik.mission.dto.RemainingProductsResponse;
import gdg.hongik.mission.dto.StockAddRequest;
import gdg.hongik.mission.dto.StockAddResponse;
import gdg.hongik.mission.entity.Product;
import gdg.hongik.mission.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {

	private final ProductRepository productRepository;

	public ProductResponse getProduct(String name) {
		if (name.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명을 입력해주세요.");
		}
		Product product = productRepository.findByName(name)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
		return ProductResponse.from(product);
	}

	public OrderResponse order(OrderRequest request) {
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
			Product product = productRepository.findById(productId)
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
			productRepository.save(product);
			totalPrice += amount;
			items.add(new OrderResponse.Item(product.getName(), quantity, amount));
		}
		return new OrderResponse(totalPrice, items);
	}

	public ProductResponse createProduct(ProductCreateRequest request) {
		if (request.name() == null || request.name().isBlank()
				|| request.price() == null || request.price() < 0
				|| request.stock() == null || request.stock() < 0) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "상품명, 가격(0 이상), 재고(0 이상)를 확인해주세요.");
		}
		if (productRepository.findByName(request.name()).isPresent()) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 상품명입니다.");
		}
		Product product = productRepository.save(new Product(request.name(), request.price(), request.stock()));
		return ProductResponse.from(product);
	}

	public StockAddResponse addStock(Long productId, StockAddRequest request) {
		if (request.quantity() == null || request.quantity() < 1) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "추가 수량은 1 이상이어야 합니다.");
		}
		Product product = productRepository.findById(productId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."));
		product.increaseStock(request.quantity());
		productRepository.save(product);
		return new StockAddResponse(product.getName(), product.getStock());
	}

	public RemainingProductsResponse deleteProducts(ProductDeleteRequest request) {
		if (request.ids() == null || request.ids().isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제할 상품 ID를 입력해주세요.");
		}
		List<Long> distinctIds = request.ids().stream().distinct().toList();
		// 하나라도 없으면 아무것도 지우지 않게 삭제 전에 전부 확인
		for (Long id : distinctIds) {
			if (!productRepository.existsById(id)) {
				throw new ResponseStatusException(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다. id=" + id);
			}
		}
		productRepository.deleteAllById(distinctIds);
		return RemainingProductsResponse.from(productRepository.findAll(Sort.by("id")));
	}
}
