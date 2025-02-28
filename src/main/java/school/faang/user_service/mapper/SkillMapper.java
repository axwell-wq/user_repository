package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.skill_dto.SkillDto;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface SkillMapper {

    Skill toEntity(SkillDto dto);

    SkillDto toDto(Skill entity);

    List<SkillDto> toDtoList(List<Skill> skills);
}
