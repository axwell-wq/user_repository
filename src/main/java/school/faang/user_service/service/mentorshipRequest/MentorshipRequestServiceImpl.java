package school.faang.user_service.service.mentorshipRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.MentorshipRequestDto;
import school.faang.user_service.dto.RejectionReasonDto;
import school.faang.user_service.entity.MentorshipRequest;
import school.faang.user_service.entity.RequestStatus;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.MapperMentorshipRequestDto;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.mentorship.MentorshipRequestRepository;
import school.faang.user_service.service.MentorshipRequestService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipRequestServiceImpl implements MentorshipRequestService {

    private final MentorshipRequestRepository mentorshipRequestRepository;
    private final UserRepository userRepository;
    private final MapperMentorshipRequestDto mapper;

    @Override
    public MentorshipRequestDto requestMentorship(MentorshipRequestDto mentorshipRequestDto) {
        checkNotSame(mentorshipRequestDto);
        existUser(mentorshipRequestDto.getReceiverId());
        existUser(mentorshipRequestDto.getRequesterId());

        mentorshipRequestDto.setStatus(RequestStatus.PENDING);

        mentorshipRequestRepository.save(mapper.toEntity(mentorshipRequestDto));

        return mentorshipRequestDto;
    }

    @Override
    public List<MentorshipRequestDto> getRequests() {
        List<MentorshipRequest> mentorshipRequestList = mentorshipRequestRepository.findAll();
        return getMentorshipRequests(mentorshipRequestList);
    }

    @Override
    public MentorshipRequestDto acceptRequest(Long id) {
        MentorshipRequest request = getMentorshipRequest(id);

        checkMentor(request);

        request.setStatus(RequestStatus.ACCEPTED);

        return mapper.toDto(mentorshipRequestRepository.save(request));
    }

    @Override
    public MentorshipRequestDto rejectRequest(Long id, RejectionReasonDto rejection) {
        MentorshipRequest request = getMentorshipRequest(id);
        request.setStatus(RequestStatus.REJECTED);
        request.setRejectionReason(rejection.getRejectionReason());

        return mapper.toDto(mentorshipRequestRepository.save(request));
    }

    private void checkMentor(MentorshipRequest request) {
        User requester = request.getRequester();
        User receiver = request.getReceiver();

        if (!requester.getMentors().contains(receiver)) {
            requester.getMentors().add(receiver);
        }

        userRepository.save(requester);
    }

    private void existUser(Long id) {
        userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found"));
    }

    private void checkNotSame(MentorshipRequestDto mentorshipRequestDto) {
        if (mentorshipRequestDto.getRequesterId().equals(mentorshipRequestDto.getReceiverId())) {
            throw new IllegalArgumentException("Requester id is the same");
        }
    }
    private List<MentorshipRequestDto> getMentorshipRequests(List<MentorshipRequest> mentorshipRequestList) {
        List<MentorshipRequestDto> mentorshipRequestDtoList = new ArrayList<>();
        for (MentorshipRequest mentorshipRequest : mentorshipRequestList) {
            mentorshipRequestDtoList.add(mapper.toDto(mentorshipRequest));
        }
        return mentorshipRequestDtoList;
    }

    private MentorshipRequest getMentorshipRequest(Long id) {
        return mentorshipRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("MentorshipRequest not found"));
    }
}
