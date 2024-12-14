package school.faang.user_service.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface MapperUserDto {

    @Mapping(source = "mentors", target = "mentorsId", ignore = true)
    @Mapping(source = "mentees", target = "menteesId", ignore = true)
    UserDto toDto(User user);

    @Mapping(source = "mentorsId", target = "mentors", ignore = true)
    @Mapping(source = "menteesId", target = "mentees", ignore = true)
    User toEntity(UserDto userDto);

    List<Long> toListIdUsers(List<User> users);

    List<UserDto> toListDtoUsers(List<User> users);

    List<User> toUserDto(List<UserDto> userDtos);
}
