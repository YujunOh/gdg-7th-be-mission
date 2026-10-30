package gdg.hongik.mission.dto;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;

public record ProductDeleteRequest(
		@Schema(description = "삭제할 상품 ID 목록 (1개 이상)", example = "[1, 2]") List<Long> ids) {
}
