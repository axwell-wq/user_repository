package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.service.RecommendationService;
import school.faang.user_service.service.RecommendationServiceImpl;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendationController {

    private final RecommendationService recommendationService;

    public void create(RecommendationDto recommendation) {
        recommendationService.create(recommendation);
    }

    public RecommendationDto updateRecommendation(RecommendationDto updated) {
        return recommendationService.update(updated);
    }

    public void deleteRecommendation(Long id) {
        recommendationService.deleteRecommendation(id);
    }

    public List<RecommendationDto> getAllUserRecommendations(long receiverId) {
        return recommendationService.getAllUserRecommendations(receiverId);
    }

    public List<RecommendationDto> getAllGivenRecommendations(long authorId) {
        return recommendationService.getAllGivenRecommendations(authorId);
    }
}
