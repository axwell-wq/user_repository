package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import org.hibernate.action.internal.EntityActionVetoException;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import school.faang.user_service.dto.RecommendationDto;
import school.faang.user_service.dto.SkillOfferDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.recommendation.Recommendation;
import school.faang.user_service.entity.recommendation.SkillOffer;
import school.faang.user_service.mapper.MapperRecommendationDto;
import school.faang.user_service.mapper.MapperSkillOfferDto;
import school.faang.user_service.repository.recommendation.RecommendationRepository;
import school.faang.user_service.repository.recommendation.SkillOfferRepository;
import school.faang.user_service.validator.Validator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private User user;
    private List<SkillOffer> skillOffers;

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
                .content("Old content")
                .skillOffers(listSkillOfferDto)
                .build();

        User user1 = new User();
        User user2 = new User();
        user1.setId(1L);
        user2.setId(2L);

        SkillOffer skillOffer1 = new SkillOffer();
        SkillOffer skillOffer2 = new SkillOffer();

        List<SkillOffer> skillOffers = List.of(skillOffer1, skillOffer2);

        recommendation = Recommendation.builder()
                .id(dto.getId())
                .author(user1)
                .receiver(user2)
                .content("Old content")
                .skillOffers(skillOffers)
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
        assertThrows(
                IllegalArgumentException.class,
                () -> recommendationService.create(dto)
        );
    }

    @Test
    public void updateFalseTest() {
        when(recommendationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EntityNotFoundException.class,
                () -> recommendationService.update(dto)
        );
    }

    @Test
    public void updateTrueTest() {
        when(recommendationRepository.findById(dto.getId())).thenReturn(Optional.of(recommendation));
        doNothing().when(skillOfferRepository).deleteAllByRecommendationId(1L);

        RecommendationDto updatedDto = new RecommendationDto();
        updatedDto.setId(1L);
        updatedDto.setContent("Test content");

        when(mapperRecommendationDto.toDto(any())).thenReturn(updatedDto);

        RecommendationDto result = recommendationService.update(dto);

        assertNotNull(result);
        assertEquals("Test content", result.getContent());
        verify(recommendationRepository, times(1)).save(any());
        verify(skillOfferRepository, times(1)).deleteAllByRecommendationId(1L);
    }

    @Test
    public void deleteRecommendationTest() {
        doNothing().when(recommendationRepository).deleteById(1L);

        recommendationService.deleteRecommendation(1L);

        verify(recommendationRepository, times(1)).deleteById(1L);
    }

    @Test
    public void getListRecommendationTest() {
        when(recommendationRepository.findByAuthorIdAndReceiverId(1L, 2L))
                .thenReturn(List.of(recommendation));

        List<Recommendation> result = recommendationService.getAllRecommendationBetweenAuthorIdAndReceiverId(dto);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Old content", result.get(0).getContent());

        verify(recommendationRepository, times(1)).findByAuthorIdAndReceiverId(1L, 2L);
    }

    @Test
    public void getRecommendationsByReceiverIdTest() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Recommendation> page = new PageImpl<>(List.of(recommendation));

        when(recommendationRepository.findAllByReceiverId(2L, pageable)).thenReturn(page);
        when(mapperRecommendationDto.toDto(recommendation)).thenReturn(dto);

        List<RecommendationDto> result = recommendationService.getRecommendationsByReceiverId(2L);

        assertNotNull(result.get(0));
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getAuthorId());
        assertEquals(2L, result.get(0).getReceiverId());
        assertEquals("Old content", result.get(0).getContent());

        verify(recommendationRepository, times(1)).findAllByReceiverId(2L, pageable);
        verify(mapperRecommendationDto, times(1)).toDto(recommendation);
    }
}