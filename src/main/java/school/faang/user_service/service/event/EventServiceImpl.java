package school.faang.user_service.service.event;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.EventDto;
import school.faang.user_service.dto.SkillDto;
import school.faang.user_service.entity.Skill;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.mapper.MapperEventDto;
import school.faang.user_service.mapper.SkillMapper;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.repository.event.EventRepository;
import school.faang.user_service.service.EventService;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final MapperEventDto mapperEventDto;
    private final SkillMapper skillMapper;

    public EventDto create(EventDto event) {
        checkSkills(event);
        eventRepository.save(mapperEventDto.toEntity(event));
        return event;
    }

    public EventDto getEvent(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> throwException());
       return mapperEventDto.toDto(event);
    }

    public void deleteEvent(Long eventId) {
        if(eventRepository.existsById(eventId)) {
            throwException();
        }
        eventRepository.deleteById(eventId);
    }

    public EventDto updateEvent(EventDto event) {
        checkSkills(event);
        Event eventEntity = eventRepository.findById(event.getId()).orElseThrow(
                () -> throwException());

        List<Skill> skillDtos = event.getRelatedSkills().stream()
                .map(skillMapper::toEntity)
                .collect(Collectors.toCollection(ArrayList::new));;

        eventEntity.setTitle(event.getTitle());
        eventEntity.setDescription(event.getDescription());
        eventEntity.setRelatedSkills(skillDtos);
        eventEntity.setLocation(event.getLocation());
        eventEntity.setMaxAttendees(event.getMaxAttendees());
        eventEntity.setType(event.getType());
        eventEntity.setStatus(event.getStatus());


        return mapperEventDto.toDto(eventRepository.save(eventEntity));
    }

    public List<EventDto> getOwnedEvents(Long userId) {
        return eventRepository.findAllByUserId(userId).stream()
                .map(event -> mapperEventDto.toDto(event))
                .toList();
    }

    public List<EventDto> getParticipatedEvents(Long userId) {
        return eventRepository.findParticipatedEventsByUserId(userId).stream()
                .map(event -> mapperEventDto.toDto(event))
                .toList();
    }

    private void checkSkills(EventDto eventDto) {
        List<Skill> skills = userRepository.findById(eventDto.getOwnerId()).orElseThrow(
                EntityNotFoundException::new).getSkills();

        Set<Long> skillsId = skills.stream()
                .map(Skill::getId)
                .collect(Collectors.toSet());

        Set<Long> skillsDtoId = eventDto.getRelatedSkills().stream()
                .map(SkillDto::getId)
                .collect(Collectors.toSet());

        if (!skillsId.containsAll(skillsDtoId)) {
            throw new DataValidationException("Не совпадают скилы");
        }
    }

    private EntityNotFoundException throwException() {
        return new EntityNotFoundException("Событие не найдено");
    }
}
