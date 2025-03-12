package school.faang.user_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.faang.user_service.entity.goal.GoalStatus;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GoalDto {
    private Long id;
    private String title;
    private String description;
    private Long parentId;
    private List<Long> skillsId;
    private GoalStatus status;
}
