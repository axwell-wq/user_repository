package school.faang.user_service.service;

import school.faang.user_service.dto.MentorshipRequestDto;
import school.faang.user_service.dto.RejectionReasonDto;
import school.faang.user_service.entity.RequestStatus;

import java.util.List;

public interface MentorshipRequestService {

    MentorshipRequestDto requestMentorship(MentorshipRequestDto mentorshipRequestDto);

    List<MentorshipRequestDto> getRequests();

    MentorshipRequestDto acceptRequest(Long id);

    MentorshipRequestDto rejectRequest(Long id, RejectionReasonDto rejectionReasonDto);
}
