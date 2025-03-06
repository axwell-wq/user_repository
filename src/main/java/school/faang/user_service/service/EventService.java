package school.faang.user_service.service;

import school.faang.user_service.dto.EventDto;

import java.util.List;

public interface EventService {

    EventDto create(EventDto event);

    EventDto getEvent(Long eventId);

    void deleteEvent(Long eventId);

    EventDto updateEvent(EventDto event);

    List<EventDto> getOwnedEvents(Long userId);

    List<EventDto> getParticipatedEvents(Long userId);
}
