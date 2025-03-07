package school.faang.user_service.service.event;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.event.Event;
import school.faang.user_service.exception.DataValidationException;
import school.faang.user_service.repository.event.EventParticipationRepository;
import school.faang.user_service.repository.event.EventRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventParticipationServiceImpl implements EventParticipationService {

    private final EventParticipationRepository eventParticipationRepository;
    private final EventRepository eventRepository;


    public void registerParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Entity not found"));

        User user = eventParticipationRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        if (event.getAttendees().contains(user)) {
            throw new DataValidationException("User not attendees");
        }
    }

    public void unregisterParticipant(Long eventId, Long userId) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException("Entity not found"));

        User user = eventParticipationRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        if (event.getAttendees().contains(user)) {
            throw new DataValidationException("User not attendees");
        } else {
            eventParticipationRepository.unregister(eventId, userId);
        }
    }

    public List<User> getParticipant(Long eventId) {
        return eventParticipationRepository.findAllParticipantsByEventId(eventId);
    }

    public int getParticipantsCount(long eventId) {
        return eventParticipationRepository.countParticipants(eventId);
    }
}

