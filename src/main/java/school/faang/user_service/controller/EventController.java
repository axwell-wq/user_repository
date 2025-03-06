package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.EventDto;
import school.faang.user_service.service.EventService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    public EventDto create(EventDto event) {
        return eventService.create(event);
    }

    public EventDto getEvent(Long eventId) {
        return eventService.getEvent(eventId);
    }

    public void deleteEvent(Long eventId) {
        eventService.deleteEvent(eventId);
    }

    public EventDto updateEvent(EventDto event) {
        return eventService.updateEvent(event);
    }

    public List<EventDto> getOwnedEvents(Long userId) {
        return eventService.getOwnedEvents(userId);
    }

    public List<EventDto> getParticipatedEvents(Long userId) {
        return eventService.getParticipatedEvents(userId);
    }
}
