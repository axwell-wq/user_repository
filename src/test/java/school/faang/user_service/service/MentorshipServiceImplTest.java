package school.faang.user_service.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import school.faang.user_service.dto.UserDto;
import school.faang.user_service.entity.User;
import school.faang.user_service.mapper.MapperUserDto;
import school.faang.user_service.repository.UserRepository;
import school.faang.user_service.service.mentorship.MentorshipServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private MapperUserDto mapperUserDto;

    @InjectMocks
    private MentorshipServiceImpl mentorshipService;

    private User user;
    private UserDto userDto;
    private User userMentees;
    private UserDto userDtoMentees;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);

        userMentees = User.builder()
                .id(3L)
                .build();

        userDtoMentees = new UserDto();
        userDtoMentees.setId(3L);

        List<User> mentees = new ArrayList<>();
        mentees.add(userMentees);

        user.setMentees(mentees);
        user.setMentors(mentees);

        userDto = new UserDto();
        userDto.setId(1L);
    }

    @Test
    public void getMenteesTrueTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapperUserDto.toDto(any())).thenReturn(userDtoMentees);

        List<UserDto> result = mentorshipService.getMentees(1L);

        assertNotNull(result.get(0));
        assertEquals(userDtoMentees, result.get(0));
        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());

        verify(userRepository, times(1)).findById(1L);
        verify(mapperUserDto, times(1)).toDto(any());
    }

    @Test
    public void getMentorsTrueTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapperUserDto.toDto(any())).thenReturn(userDtoMentees);

        List<UserDto> result = mentorshipService.getMentors(1L);

        assertNotNull(result.get(0));
        assertEquals(userDtoMentees, result.get(0));
        assertEquals(1, result.size());
        assertEquals(3L, result.get(0).getId());

        verify(userRepository, times(1)).findById(1L);
        verify(mapperUserDto, times(1)).toDto(any());
    }

    @Test
    public void deleteMenteeTrueTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userMentees));

        mentorshipService.deleteMentee(3L, 1L);

        assertFalse(user.getMentees().contains(userMentees));

        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void deleteMentorTrueTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.findById(3L)).thenReturn(Optional.of(userMentees));

        mentorshipService.deleteMentor(1L, 3L);

        assertFalse(user.getMentors().contains(userMentees));

        verify(userRepository, times(1)).save(user);
    }
}