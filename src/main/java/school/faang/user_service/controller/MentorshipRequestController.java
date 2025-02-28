package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import school.faang.user_service.dto.MentorshipRequestDto;
import school.faang.user_service.dto.RejectionReasonDto;
import school.faang.user_service.mapper.MapperMentorshipRequestDto;
import school.faang.user_service.service.MentorshipRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    public MentorshipRequestDto requestMentorship(MentorshipRequestDto requestDto) {
        return mentorshipRequestService.requestMentorship(requestDto);
    }

    public List<MentorshipRequestDto> getRequests() {
        return mentorshipRequestService.getRequests();
    }

    public MentorshipRequestDto acceptRequest(Long id) {
        return mentorshipRequestService.acceptRequest(id);
    }

    public MentorshipRequestDto rejectRequest(Long id, RejectionReasonDto rejectionReasonDto) {
        return mentorshipRequestService.rejectRequest(id, rejectionReasonDto);
    }

}
