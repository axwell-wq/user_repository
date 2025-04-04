package school.faang.user_service.dto;


import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import school.faang.user_service.entity.User;
import lombok.AllArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private List<Long> mentorsId;
    private List<Long> menteesId;
}
