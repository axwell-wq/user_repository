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
import school.faang.user_service.repository.mentorship.MentorshipRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MentorshipServiceImplTest {

    @Mock
    private MentorshipRepository mentorshipRepository;

    @Mock
    private UserRepository userRepository;

    @Spy
    private MapperUserDto mapperUserDto;

    @InjectMocks
    private MentorshipServiceImpl mentorshipService;

    private User user;
    private UserDto userDto;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);

        userDto = new UserDto();
        userDto.setId(1L);
    }

    @Test
    public void getMenteesTrueTest() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(mapperUserDto.toListDtoUsers(any())).thenReturn(List.of(userDto));

        List<UserDto> result = mentorshipService.getMentees(1L);

        assertNotNull(result.get(0));
        assertEquals(userDto, result.get(0));
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).getId());

        verify(userRepository, times(1)).findById(1L);
        verify(mapperUserDto, times(1)).toListDtoUsers(any());
    }

}