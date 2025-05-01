package school.faang.user_service.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.entity.UserProfilePic;
import school.faang.user_service.mapper.MapperUserDto;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.AvatarService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserRepository userRepository;
    private final MapperUserDto mapperUserDto;
    private final AvatarService avatarService;

    @PostMapping("/{userId}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {
        try {
            UserProfilePic result = avatarService.createUserAvatar(file, userId);
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Failed to upload avatar");
        }
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {

        try {
            UserProfilePic result = avatarService.updateUserAvatar(file, userId);
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Failed to update avatar", e);
            return ResponseEntity.internalServerError().body("Failed to update avatar");
        }
    }

    @DeleteMapping("/{userId}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        try {
            avatarService.deleteUserAvatar(userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Failed to delete avatar", e);
            return ResponseEntity.internalServerError().body("Failed to delete avatar");
        }
    }

    @GetMapping("{userId}")
    public UserDto getUser(@PathVariable Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException("User not found"));

        return UserDto.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();
    }


    @GetMapping("/getusersbyids")
    public List<UserDto> getUsersByIds(@RequestParam List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);

        return users.stream()
                .map(mapperUserDto::toDto)
                .toList();
    }
}
