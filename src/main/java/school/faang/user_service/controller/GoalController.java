package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.GoalDto;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.service.GoalService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/goal")
public class GoalController {

    private final GoalService goalService;

    @PostMapping("/create/{userId}")
    GoalDto createGoal(@PathVariable Long userId,@RequestBody GoalDto goal) {
        return goalService.createGoal(userId, goal);
    }

    @PutMapping("/updatee/{goalId}")
    GoalDto updateeGoal(@PathVariable Long goalId,@RequestBody GoalDto goalDto) {
        return goalService.updateeGoal(goalId, goalDto);
    }

    @DeleteMapping("/delete/{goalId}")
    void deleteGoal(@PathVariable Long goalId) {
        goalService.deleteGoal(goalId);
    }

    @GetMapping("/findSubtasksByGoalId/{goalId}")
    List<Goal> findSubtasksByGoalId(@PathVariable Long goalId) {
        return goalService.findSubtasksByGoalId(goalId);
    }

    @GetMapping("/getGoalsByUser/{userId}")
    List<Goal> getGoalsByUser(@PathVariable Long userId) {
        return goalService.getGoalsByUser(userId);
    }
}
