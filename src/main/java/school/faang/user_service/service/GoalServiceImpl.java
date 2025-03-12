package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.GoalDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.goal.Goal;
import school.faang.user_service.entity.goal.GoalStatus;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.MapperGoalDto;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.goal.GoalRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final SkillRepository skillRepository;
    private final UserRepository userRepository;
    private final MapperGoalDto mapperGoalDto;

    public GoalDto createGoal(Long userId, GoalDto goal) {
        validateAmountGoals(userId);
        validateExistsSkills(goal);

        addAllSkills(goal);

        return mapperGoalDto.toDto(goalRepository.create(goal.getTitle(), goal.getDescription(), goal.getParentId()));
    }

    public GoalDto updateeGoal(Long goalId, GoalDto goalDto) {
        Goal goal = goalRepository.findById(goalId).orElseThrow(
                () -> new EntityNotFoundException("цель не найдена"));
        if (goal.getStatus() == GoalStatus.COMPLETED) {
            throw new IllegalArgumentException("цель уже завершена");
        }

        goal.setTitle(goalDto.getTitle());
        goal.setDescription(goalDto.getDescription());
        goal.setParent(goalRepository.findById(goalDto.getParentId()).orElseThrow(
                () -> new EntityNotFoundException("parent не найден ")));
        goal.se
    }

    private void validateAmountGoals(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        if (user.getGoals().size() > 2) {
            throw new DataValidationException("Еhe number of targets has reached the limit");
        }
    }

    private void validateExistsSkills(GoalDto goal) {
        for (Long id : goal.getSkillsId()) {
            if (!skillRepository.existsById(id)) {
                throw new IllegalArgumentException("Non-existent ability");
            }
        }
    }

    private void addAllSkills(GoalDto goal) {
        List<Skill> skills = new ArrayList<>();

        for (Long id : goal.getSkillsId()) {
            skills.add(skillRepository.findById(id).orElseThrow(
                    () -> new EntityNotFoundException("Skill not found")));
        }

        for (Skill skill : skills) {
            goalRepository.addSkillToGoal(skill.getId(), goal.getId());
        }
    }
}
