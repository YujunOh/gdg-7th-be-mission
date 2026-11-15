package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDeleteRequest(
		@Schema(description = "삭제할 상품명", example = "원피스") String itemName) {
}
