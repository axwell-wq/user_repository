package school.faang.user_service.controller.event;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import school.faang.user_service.entity.User;
import school.faang.user_service.service.event.EventParticipationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/eventparticipation")
public class EventParticipationController {

    private final EventParticipationService eventParticipationService;

    @PostMapping("/register/{eventId}/{userId}")
    public void registerParticipant(@PathVariable Long eventId,@PathVariable Long userId) {
        eventParticipationService.registerParticipant(eventId, userId);
    }

    @DeleteMapping("/unregister/{eventId}/{userId}")
    public void unregisterParticipant(@PathVariable Long eventId,@PathVariable Long userId) {
        eventParticipationService.unregisterParticipant(eventId, userId);
    }

    @GetMapping("/getparticipant/{eventId}")
    public List<User> getParticipant(@PathVariable Long eventId) {
        return eventParticipationService.getParticipant(eventId);
    }

    @GetMapping("/getparticipantscount/{eventId}")
    public int getParticipantsCount(@PathVariable Long eventId) {
        return eventParticipationService.getParticipantsCount(eventId);
    }
}
