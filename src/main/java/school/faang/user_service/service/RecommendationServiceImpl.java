package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.mapper.MapperRecommendationDto;
import school.faang.user_service.mapper.MapperSkillOfferDto;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;
import school.faang.user_service.validator.Validator;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class RecommendationServiceImpl implements RecommendationService {
    private final RecommendationRepository recommendationRepository;
    private final SkillOfferRepository skillOfferRepository;
    private final Validator validator;
    private final MapperSkillOfferDto mapperSkillOfferDto;
    private final MapperRecommendationDto mapperRecommendationDto;

    @Override
    public void create(RecommendationDto recommendation) {
        validator.giveRecommendation(recommendation);
        validator.spamCheck(recommendation);
        existsSkillOffer(recommendation);
        recommendationRepository.create(recommendation.getAuthorId(), recommendation.getReceiverId(),
                recommendation.getContent());

        for (SkillOfferDto dto : recommendation.getSkillOffers()) {
            skillOfferRepository.save(mapperSkillOfferDto.toEntity(dto));
        }
    }

    @Override
    public RecommendationDto update(RecommendationDto recommendationDto) {
        Recommendation recommendationEntity = recommendationRepository.findById(recommendationDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("recommendationDto not found"));

        checkAuthentication(recommendationDto, recommendationEntity);

        skillOfferRepository.deleteAllByRecommendationId(recommendationDto.getId());

        recommendationEntity.setContent(recommendationDto.getContent());
        recommendationEntity.setSkillOffers(mapSkillOffersDto(recommendationDto));

        return mapperRecommendationDto.toDto(recommendationRepository.save(recommendationEntity));
    }

    @Override
    public void deleteRecommendation(Long id) {
        recommendationRepository.deleteById(id);
    }

    @Override
    public List<Recommendation> getAllRecommendationBetweenAuthorIdAndReceiverId(RecommendationDto recommendation) {
        return recommendationRepository
                .findByAuthorIdAndReceiverId(recommendation.getAuthorId(), recommendation.getReceiverId());
    }

    @Override
    public List<RecommendationDto> getAllUserRecommendations(Long receiverId) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recommendation> page = recommendationRepository.findAllByReceiverId(receiverId, pageable);

        return getListRecommendationDto(page);
    }

    @Override
    public List<RecommendationDto> getAllGivenRecommendations(Long authorId) {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recommendation> page = recommendationRepository.findAllByAuthorId(authorId, pageable);

        return getListRecommendationDto(page);
    }

    private List<RecommendationDto> getListRecommendationDto(Page<Recommendation> page) {
        List<RecommendationDto> outList = new ArrayList<>();

        for (Recommendation recommendation : page) {
            outList.add(mapperRecommendationDto.toDto(recommendation));
        }

        return outList;
    }

    private void existsSkillOffer(RecommendationDto recommendation) {
        for (SkillOfferDto skill : recommendation.getSkillOffers()) {
            if (!skillOfferRepository.existsById(skill.getId())) {
                throw new IllegalArgumentException("Skill offer does not exist");
            }
        }
    }

    private List<SkillOffer> mapSkillOffersDto(RecommendationDto recommendation) {
        List<SkillOffer> skillOffers = new ArrayList<>();

        for (SkillOfferDto dto : recommendation.getSkillOffers()) {
            skillOffers.add(mapperSkillOfferDto.toEntity(dto));
        }

        return skillOffers;
    }

    private void checkAuthentication(RecommendationDto recommendation, Recommendation recommendationEntity) {
        if (!recommendation.getAuthorId().equals(recommendationEntity.getAuthor().getId())) {
            throw new IllegalArgumentException("Author id mismatch");
        }
    }
}
