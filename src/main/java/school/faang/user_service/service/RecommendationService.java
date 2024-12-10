package school.faang.user_service.service;

import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.entity.recommendation.Recommendation;

import java.util.List;

public interface RecommendationService {

    void create(RecommendationDto recommendationDto);

    RecommendationDto update(RecommendationDto recommendationDto);

    List<Recommendation> getAllRecommendationBetweenAuthorIdAndReceiverId(RecommendationDto recommendation);
}
