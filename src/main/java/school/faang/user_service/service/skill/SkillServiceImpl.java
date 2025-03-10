package school.faang.user_service.service.skill;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.User;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.SkillMapper;
import school.faang.user_service.repository.SkillRepository;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.dto.SkillDto;
import school.faang.user_service.service.SkillService;
import school.faang.user_service.validator.recommendation.SkillValidator;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;
    private final SkillMapper skillMapper;
    private final SkillValidator skillValidator;
    private final UserRepository userRepository;

    @Override
    public SkillDto create(SkillDto skillDto) {
        validateExistSkill(skillDto);
        skillValidator.validateSkill(skillDto);

        Skill skillEntity = skillMapper.toEntity(skillDto);

        return skillMapper.toDto(skillRepository.save(skillEntity));
    }

    @Override
    public List<SkillDto> getUserSkills(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        return skillMapper.toDtoList(user.getSkills());
    }

    private void validateExistSkill(SkillDto skillDto) {
        if (skillRepository.existsByTitle(skillDto.getTitle())) {
            throw new DataValidationException("This title is exist");
        }
    }
}
