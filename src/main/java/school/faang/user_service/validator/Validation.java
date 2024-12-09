package school.faang.user_service.validator;

import org.springframework.stereotype.Component;
import school.faang.user_service.dto.RecommendationDto;

@Component
public class Validation {
    public void giveRecommendation(RecommendationDto recommendation) {
        if(recommendation.getContent() == null || recommendation.getContent().isBlank()) {
            throw new IllegalArgumentException("this content is blank");
        }
    }
}
