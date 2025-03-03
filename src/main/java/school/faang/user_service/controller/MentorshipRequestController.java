package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.MentorshipRequestDto;
import school.faang.user_service.dto.RejectionReasonDto;
import school.faang.user_service.mapper.MapperMentorshipRequestDto;
import school.faang.user_service.service.MentorshipRequestService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/mentorship")
public class MentorshipRequestController {

    private final MentorshipRequestService mentorshipRequestService;

    @PostMapping("/requestmentor")
    public MentorshipRequestDto requestMentorship(@RequestBody MentorshipRequestDto requestDto) {
        return mentorshipRequestService.requestMentorship(requestDto);
    }

    @GetMapping("/requests")
    public List<MentorshipRequestDto> getRequests() {
        return mentorshipRequestService.getRequests();
    }

    @PutMapping("/accept/{id}")
    public MentorshipRequestDto acceptRequest(@PathVariable Long id) {
        return mentorshipRequestService.acceptRequest(id);
    }

    @PutMapping("/reject/{id}")
    public MentorshipRequestDto rejectRequest(@PathVariable Long id,@RequestBody RejectionReasonDto rejectionReasonDto) {
        return mentorshipRequestService.rejectRequest(id, rejectionReasonDto);
    }

}
