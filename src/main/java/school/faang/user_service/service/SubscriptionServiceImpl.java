package school.faang.user_service.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.MapperUserDto;
import school.faang.user_service.repository.SubscriptionRepository;
import school.faang.user_service.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final UserRepository userRepository;
    private final MapperUserDto userMapper;

    public void followUser(Long followerId, Long followeeId) {
        if (subscriptionRepository.existsByFollowerIdAndFolloweeId(followerId, followeeId)) {
            throw new EntityNotFoundException("You have already subscribed");
        }
        else {
            subscriptionRepository.followUser(followerId, followeeId);
        }
    }

    public void unfollowUser(Long followerId, Long followeeId) {
        subscriptionRepository.unfollowUser(followerId, followeeId);
    }

    public List<UserDto> getFollowers(Long followerId) {
        return getUsersAndMapToDto(followerId);
    }

    public int getFollowersCount(Long followerId) {
        return subscriptionRepository.findFolloweesAmountByFollowerId(followerId);
    }

    public List<UserDto> getFollowees(Long followerId) {
        return getUsersAndMapToDto(followerId);
    }

    public int getFollowingCount(Long followerId) {
        return subscriptionRepository.findFollowersAmountByFolloweeId(followerId);
    }

    private List<UserDto> getUsersAndMapToDto(Long followerId) {
        List<User> userList = userRepository.findById(followerId).orElseThrow(
                        () -> new EntityNotFoundException("User not found"))
                .getFollowers();

        return userList.stream()
                .map(userMapper::toDto)
                .toList();
    }
}
