package school.faang.user_service.validator.recommendation;

import org.springframework.stereotype.Component;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.dto.SkillDto;

@Component
public class SkillValidator {
    public void validateSkill(SkillDto skill) {
        if (skill.getTitle() == null || skill.getTitle().isBlank()) {
            throw new DataValidationException("Skill title is blank");
        }
    }
}
