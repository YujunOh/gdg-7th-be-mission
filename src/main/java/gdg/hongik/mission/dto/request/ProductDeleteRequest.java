package gdg.hongik.mission.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ProductDeleteRequest(
		@Schema(description = "삭제할 상품명", example = "원피스")
		@NotBlank(message = "삭제할 상품명을 입력해주세요.") String itemName) {
}
