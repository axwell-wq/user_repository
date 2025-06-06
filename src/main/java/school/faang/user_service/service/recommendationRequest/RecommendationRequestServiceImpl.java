package school.faang.user_service.service.recommendationRequest;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.RequestFilterDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.mapper.RecommendationRequestMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;
import school.faang.user_service.repository.recommendation.SkillRequestRepository;
import school.faang.user_service.service.RecommendationRequestService;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static school.faang.user_service.entity.RequestStatus.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestServiceImpl implements RecommendationRequestService {

    private final RecommendationRequestRepository recommendationRequestRepository;
    private final UserRepository userRepository;
    private final SkillRequestRepository skillRequestRepository;
    private final RecommendationRequestMapper mapper;

    @Override
    public void create(RecommendationRequestDto recommendationRequestDto) {
        validationExistsRequesterAndReceiver(recommendationRequestDto);
        spamCheck(recommendationRequestDto);
        existsSkills(recommendationRequestDto);

        RecommendationRequest request = mapper.toEntity(recommendationRequestDto);

        recommendationRequestRepository.save(request);
        skillRequestRepository.saveAll(request.getSkills());
    }

    @Override
    public List<RecommendationRequestDto> getRecommendationRequests(RequestFilterDto filters) {
        List<RecommendationRequest> outRequests = filterRecommendationRequests(filters);

        return outRequests.stream()
                .map(mapper::toDto)
                .toList();
    }

    @Override
    public RecommendationRequestDto getRecommendationRequest(Long id) {
        return mapper.toDto(recommendationRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        ));
    }

    @Override
    public void rejectRequest(Long id, RejectionDto rejectionDto) {
        RecommendationRequest request = recommendationRequestRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Request not found")
        );

        if (!request.getStatus().equals(REJECTED)) {
            request.setStatus(REJECTED);
            request.setRejectionReason(rejectionDto.getRejectionReason());
        }
    }

    private void validationExistsRequesterAndReceiver(RecommendationRequestDto recommendationRequestDto) {
        if (!userRepository.existsById(recommendationRequestDto.getRequesterId())) {
            log.error("requester id {} does not exist", recommendationRequestDto.getRequesterId());
            throw new EntityNotFoundException();
        }

        if (!userRepository.existsById(recommendationRequestDto.getReceiverId())) {
            log.error("receiver id {} does not exist", recommendationRequestDto.getReceiverId());
            throw new EntityNotFoundException();
        }
    }

    private void spamCheck(RecommendationRequestDto recommendationRequestDto) {
        User requester = getUserById(recommendationRequestDto.getRequesterId());
        User receiver = getUserById(recommendationRequestDto.getReceiverId());

        List<RecommendationRequest> requests = recommendationRequestRepository
                .findByRequesterIdAndReceiverId(requester.getId(), receiver.getId());

        for (RecommendationRequest recommendationRequest : requests) {
            long dateTimeRequest = ChronoUnit.MONTHS.between(recommendationRequest.getCreatedAt(),
                    recommendationRequestDto.getCreatedAt());

            if (dateTimeRequest < 6) {
                throw new IllegalArgumentException("It has not been 6 months since the last request");
            }
        }
    }

    private User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("User not found"));
    }

    private void existsSkills(RecommendationRequestDto recommendationRequestDto) {
        for (Long id : recommendationRequestDto.getIdSkills()) {
            if (!skillRequestRepository.existsById(id)) {
                log.error("skill {} does not exist", id);
                throw new EntityNotFoundException();
            }
        }
    }

    private List<RecommendationRequest> filterRecommendationRequests(RequestFilterDto filters) {
        List<RecommendationRequest> requests = recommendationRequestRepository.findAll();
        List<RecommendationRequest> outRequests = new ArrayList<>();

        for (RecommendationRequest request : requests) {
            if (filters.getRequesterName() != null && request.getRequester().getUsername().contains(filters.getRequesterName())) {
                outRequests.add(request);
            }

            if (filters.getReceiverName() != null && request.getReceiver().getUsername().contains(filters.getReceiverName())) {
                outRequests.add(request);
            }
        }

        return outRequests;
    }
}
