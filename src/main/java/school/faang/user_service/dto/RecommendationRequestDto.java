package school.faang.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import school.faang.user_service.entity.RequestStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class RecommendationRequestDto {
    private Long id;
    private Long requesterId;
    private Long receiverId;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private RequestStatus status;
    private List<Long> idSkills;
}
