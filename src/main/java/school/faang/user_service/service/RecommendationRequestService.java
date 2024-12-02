package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.mapper.RecommendationRequestMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.recommendation.RecommendationRequestRepository;
import school.faang.user_service.repository.recommendation.SkillRequestRepository;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecommendationRequestService {

    private final RecommendationRequestRepository recommendationRequestRepository;
    private final UserRepository userRepository;
    private final SkillRequestRepository skillRequestRepository;
    private final RecommendationRequestMapper mapper;

    public void create(RecommendationRequestDto recommendationRequestDto) {
        validationExistsRequesterAndReceiver(recommendationRequestDto);
        spamCheck(recommendationRequestDto);
        existsSkills(recommendationRequestDto);

        RecommendationRequest request = mapper.toEntity(recommendationRequestDto);

        recommendationRequestRepository.save(request);
        skillRequestRepository.saveAll(request.getSkills());
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
}
