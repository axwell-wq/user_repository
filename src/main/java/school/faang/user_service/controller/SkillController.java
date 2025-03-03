package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.service.SkillService;
import school.faang.user_service.dto.SkillDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/skills")
public class SkillController {
    private final SkillService skillService;

    @PostMapping("/create")
    public SkillDto create(@RequestBody SkillDto skillDto) {
        return skillService.create(skillDto);
    }

    @GetMapping("/user/{userId}")
    public List<SkillDto> getUserSkills(@PathVariable Long userId) {
        return skillService.getUserSkills(userId);
    }
}
