package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.service.RecommendationService;
import school.faang.user_service.service.RecommendationServiceImpl;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/recommendationcontroller")
public class RecommendationController {

    private final RecommendationService recommendationService;

    @PostMapping("/create")
    public void create(@RequestBody RecommendationDto recommendation) {
        recommendationService.create(recommendation);
    }

    @PutMapping("/updateRecommendation")
    public RecommendationDto updateRecommendation(@RequestBody RecommendationDto updated) {
        return recommendationService.update(updated);
    }

    @DeleteMapping("/deleteecommendation{id}")
    public void deleteRecommendation(@PathVariable Long id) {
        recommendationService.deleteRecommendation(id);
    }

    @GetMapping("/getrecommendationsbyreceiverId{receiverId}")
    public List<RecommendationDto> getRecommendationsByReceiverId(@PathVariable Long receiverId) {
        return recommendationService.getRecommendationsByReceiverId(receiverId);
    }

    @GetMapping("/getrecommendationsbyauthorId{authorId}")
    public List<RecommendationDto> getRecommendationsByAuthorId(@PathVariable Long authorId) {
        return recommendationService.getRecommendationsByReceiverId(authorId);
    }
}
