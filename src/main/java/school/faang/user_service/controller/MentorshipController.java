package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.service.MentorshipService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class MentorshipController {

    private final MentorshipService service;

    public List<UserDto> getMentees(Long userId) {
        return service.getMentees(userId);
    }

    public List<UserDto> getMentors(Long userId) {
        return service.getMentors(userId);
    }

    public void deleteMentee(Long menteeId, Long mentorId) {
        service.deleteMentee(menteeId, mentorId);
    }

    public void deleteMentor(Long menteeId, Long mentorId) {
        service.deleteMentor(menteeId, mentorId);
    }
}
