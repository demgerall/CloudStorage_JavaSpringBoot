package ru.netology.CloudStorage_SpringBoot.testServices;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.netology.CloudStorage_SpringBoot.entities.Role;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.repositories.UserRepository;
import ru.netology.CloudStorage_SpringBoot.security.UserDetailsService;
import ru.netology.CloudStorage_SpringBoot.utils.exeptions.UsernameNotFoundException;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

public class TestUserDetailsService {

    @InjectMocks
    private UserDetailsService userDetailsService;

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        openMocks(this);
    }

    @Test
    void loadUserByUsername() {

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(new User("name", "pass", Role.ROLE_USER)));
        userDetailsService.loadUserByUsername(anyString());

        verify(userRepository, times(1)).findByLogin(any());
    }

    @Test
    void loadUserByUsername_UsernameNotFoundException() {

        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());

        Assert.assertThrows(UsernameNotFoundException.class,

                () -> {
                    userDetailsService.loadUserByUsername(anyString());
                }

        );
    }
}
