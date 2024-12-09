package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;
import school.faang.user_service.validator.Validation;

import java.time.temporal.ChronoUnit;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final Validation validation;

    public void create(RecommendationDto recommendation) {
        validation.giveRecommendation(recommendation);
        spamCheck(recommendation);
    }

    private void spamCheck(RecommendationDto recommendation) {
        List<Recommendation> recommendationList = recommendationRepository
                .findByAuthorIdAndReceiverId(recommendation.getAuthorId(), recommendation.getReceiverId());

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
