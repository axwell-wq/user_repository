package school.faang.user_service.service;

import school.faang.user_service.dto.RecommendationRequestDto;
import school.faang.user_service.dto.RejectionDto;
import school.faang.user_service.dto.RequestFilterDto;

import java.util.List;

public interface RecommendationRequestService {

    void create(RecommendationRequestDto recommendationRequestDto);

    List<RecommendationRequestDto> getRecommendationRequests(RequestFilterDto filters);

    RecommendationRequestDto getRecommendationRequest(Long id);

    void rejectRequest(Long id, RejectionDto rejectionDto);
}
