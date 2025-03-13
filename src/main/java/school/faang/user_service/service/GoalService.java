package school.faang.user_service.service;

import school.faang.user_service.dto.GoalDto;
import school.faang.user_service.entity.goal.Goal;

import java.util.List;

public interface GoalService {

    GoalDto createGoal(Long userId, GoalDto goal);

    GoalDto updateeGoal(Long goalId, GoalDto goalDto);

    void deleteGoal(Long goalId);

    List<Goal> findSubtasksByGoalId(Long goalId);

    List<Goal> getGoalsByUser(Long userId);
}
