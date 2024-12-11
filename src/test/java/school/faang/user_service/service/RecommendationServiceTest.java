package school.faang.user_service.service;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.mapper.MapperRecommendationDto;
import school.faang.user_service.mapper.MapperSkillOfferDto;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;
import school.faang.user_service.validator.Validator;

import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecommendationServiceTest {

    @Mock
    private RecommendationRepository recommendationRepository;

    @Mock
    private SkillOfferRepository skillOfferRepository;

    @Mock
    private Validator validator;

    @Spy
    private MapperSkillOfferDto mapperSkillOfferDto;

    @Spy
    private MapperRecommendationDto mapperRecommendationDto;

    @InjectMocks
    private RecommendationServiceImpl recommendationService;

    private SkillOfferDto firstSkillDto;
    private SkillOfferDto secondSkillDto;
    private RecommendationDto dto;
    private Recommendation recommendation;

    @BeforeEach
    public void setUp() {
        firstSkillDto = SkillOfferDto.builder()
                .id(1L)
                .skillId(1L)
                .recommendationId(1L)
                .build();

        secondSkillDto = SkillOfferDto.builder()
                .id(2L)
                .skillId(2L)
                .recommendationId(1L)
                .build();

        List<SkillOfferDto> listSkillOfferDto = List.of(secondSkillDto, firstSkillDto);

        dto = RecommendationDto.builder()
                .id(1L)
                .authorId(1L)
                .receiverId(2L)
                .content("Test content")
                .skillOffers(listSkillOfferDto)
                .build();
    }

    @Test
    public void createTrueTest() {
        when(skillOfferRepository.existsById(1L)).thenReturn(true);
        when(skillOfferRepository.existsById(2L)).thenReturn(true);
        when(recommendationRepository.create(dto.getAuthorId(), dto.getReceiverId(), dto.getContent()))
                .thenReturn(1L);

        recommendationService.create(dto);
    }

    @Test
    public void createFalseTest() {
        Assert.assertThrows(
                IllegalArgumentException.class,
                () -> recommendationService.create(dto)
        );
    }
}