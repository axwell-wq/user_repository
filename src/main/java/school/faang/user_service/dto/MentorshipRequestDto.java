package school.faang.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.faang.user_service.entity.RequestStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MentorshipRequestDto {

    private String description;
    private Long requesterId;
    private Long receiverId;
    private RequestStatus status;
    private String rejectionReason;

}
