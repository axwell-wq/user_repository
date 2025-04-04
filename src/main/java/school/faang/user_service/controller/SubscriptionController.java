package school.faang.user_service.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.service.SubscriptionService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    public void unfollowUser(Long followerId, Long followeeId) {
        subscriptionService.unfollowUser(followerId, followeeId);
    }

    public void followUser(Long followerId, Long followeeId) {
        subscriptionService.followUser(followerId, followeeId);
    }

    public void getFollowersCount(Long followeeId) {
        subscriptionService.getFollowersCount(followeeId);
    }

    public void getFollowersCount(long followerId) {
        subscriptionService.getFollowersCount(followerId);
    }

    public List<UserDto> getFollowers(Long followerId) {
        return subscriptionService.getFollowers(followerId);
    }

    public List<UserDto> getFollowees(Long followerId) {
        return subscriptionService.getFollowees(followerId);
    }

    public int getFollowingCount(Long followerId) {
        return subscriptionService.getFollowingCount(followerId);
    }
}
