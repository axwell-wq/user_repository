package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.service.SkillService;
import school.faang.user_service.dto.SkillDto;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    public SkillDto create(SkillDto skillDto) {
        return skillService.create(skillDto);
    }

    public List<SkillDto> getUserSkills(Long userId) {
        return skillService.getUserSkills(userId);
    }
}
