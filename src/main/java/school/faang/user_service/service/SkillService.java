package school.faang.user_service.service;

import school.faang.user_service.dto.SkillDto;

import java.util.List;

public interface SkillService {
    SkillDto create(SkillDto skillDto);
    List<SkillDto> getUserSkills(Long userId);
}
