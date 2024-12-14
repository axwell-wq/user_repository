package school.faang.user_service.service;

import school.faang.user_service.dto.UserDto;

import java.util.List;

public interface MentorshipService {

    List<UserDto> getMentees(Long userId);

    List<UserDto> getMentors(Long userId);

    void deleteMentee(Long menteeId, Long mentorId);

    void deleteMentor(Long menteeId, Long mentorId);
}
