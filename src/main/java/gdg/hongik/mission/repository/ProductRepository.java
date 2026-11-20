package gdg.hongik.mission.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import gdg.hongik.mission.entity.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

	Optional<Product> findByName(String name);
}
