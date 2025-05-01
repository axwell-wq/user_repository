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
            log.info("Аватара успешно создан");
            return ResponseEntity.ok(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            log.info("Ошибка создание аватара");
            return ResponseEntity.internalServerError().body("Ошибка создания аватара");
        }
    }

    @PutMapping("/{userId}/avatar")
    public ResponseEntity<?> updateAvatar(@PathVariable Long userId, @RequestParam("file") MultipartFile file) {

        try {
            UserProfilePic result = avatarService.updateUserAvatar(file, userId);
            log.info("Аватар успешно обновлен");
            return ResponseEntity.ok(result);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            log.error("Не удалось обновить аватар", e);
            return ResponseEntity.internalServerError().body("Не удалось обновить аватар");
        }
    }

    @DeleteMapping("/{userId}/avatar")
    public ResponseEntity<?> deleteAvatar(@PathVariable Long userId) {
        try {
            avatarService.deleteUserAvatar(userId);
            log.info("Удаление аватара успешно завершено для пользователя с идентификатором: {}", userId);
            return ResponseEntity.ok().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            log.error("Ошибка удаления файла", e);
            return ResponseEntity.internalServerError().body("Ошибка удаления файла");
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
