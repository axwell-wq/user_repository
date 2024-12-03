package school.faang.user_service.controller.recommendation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.RequestFilterDto;
import school.faang.user_service.entity.recommendation.RecommendationRequest;
import school.faang.user_service.service.RecommendationRequestService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendationRequestController {

    private final RecommendationRequestService recommendationRequestService;

    public void create(RecommendationRequestDto recommendationRequestDto) {
        recommendationRequestService.create(recommendationRequestDto);
    }

    public List<RecommendationRequestDto> getRecommendationRequests(RequestFilterDto filter) {
        return recommendationRequestService.getRecommendationRequests(filter);
    }

    public RecommendationRequestDto getRecommendationRequest(Long id) {
        return recommendationRequestService.getRecommendationRequest(id);
    }

    public void rejectRequest(Long id, RejectionDto rejectionDto) {
        recommendationRequestService.rejectRequest(id, rejectionDto);
    }
}
