package school.faang.user_service.service;

import school.faang.user_service.dto.UserDto;

import java.util.List;

public interface SubscriptionService {

    void followUser(Long followerId, Long followeeId);

    void unfollowUser(Long followerId, Long followeeId);

    int getFollowersCount(Long followeeId);

    List<UserDto> getFollowers(Long followerId);

    List<UserDto> getFollowees(Long followerId);

    int getFollowingCount(Long followerId);
}
