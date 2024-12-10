package school.faang.user_service.validator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.service.RecommendationService;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
@RequiredArgsConstructor
public class Validation {

    private final RecommendationService recommendationService;

    public void giveRecommendation(RecommendationDto recommendation) {
        if(recommendation.getContent() == null || recommendation.getContent().isBlank()) {
            throw new IllegalArgumentException("this content is blank");
        }
    }

    public void spamCheck(RecommendationDto recommendation) {
        List<Recommendation> recommendationList =
                recommendationService.getAllRecommendationBetweenAuthorIdAndReceiverId(recommendation);

        recommendationList
                .forEach(recomm -> {
                    long dateTimeRecomm = ChronoUnit.MONTHS.between(recomm.getCreatedAt(),
                            recommendation.getCreatedAt());

                    if (dateTimeRecomm < 6) {
                        throw new IllegalArgumentException("It has not been 6 months since the last recommendation");
                    }
                });
    }
}
