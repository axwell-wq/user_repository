package school.faang.user_service.service.mentorship;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.MapperUserDto;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.MentorshipService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MentorshipServiceImpl implements MentorshipService {

    private final MapperUserDto userMapper;
    private final UserRepository userRepository;

    @Override
    public List<UserDto> getMentees(Long userId) {
        return getUserById(userId).getMentees().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public List<UserDto> getMentors(Long userId) {
        return getUserById(userId).getMentors().stream()
                .map(userMapper::toDto)
                .toList();
    }

    @Override
    public void deleteMentee(Long menteeId, Long mentorId) {
        User user = getUserById(mentorId);
        user.getMentees().remove(getUserById(menteeId));

        userRepository.save(user);
    }

    @Override
    public void deleteMentor(Long menteeId, Long mentorId) {
        User user = getUserById(menteeId);
        user.getMentors().remove(getUserById(mentorId));

        userRepository.save(user);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("User not found"));
    }
}
