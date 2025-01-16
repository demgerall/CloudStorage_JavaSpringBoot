package ru.netology.CloudStorage_SpringBoot.testServices;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.netology.CloudStorage_SpringBoot.entities.Role;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.repositories.UserRepository;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

public class TestUserService {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private User userStub;

    private final String defaultUsername = "user";
    private final Role defaultRole = Role.valueOf("ROLE_USER");

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void willShowTrueIfSaveUser() {
        String defaultPassword = "pass";
        when(userRepository.save(any(User.class))).thenReturn(
                new User(defaultUsername, defaultPassword, defaultRole)
        );

        User savedUser = userRepository.save(userStub);

        Assertions.assertNotNull(savedUser);
        verify(userRepository, times(1)).save(any(User.class));

    }

    @Test
    void testFindByUsernameShouldBeNotNull() {

        when(userRepository.findByLogin(defaultUsername))
                .thenReturn(Optional.of(userStub));

        User userExpected = userService.findByUsername(defaultUsername);
        Assertions.assertNotNull(userExpected);

        verify(userRepository, times(1)).findByLogin(defaultUsername);
    }
}
