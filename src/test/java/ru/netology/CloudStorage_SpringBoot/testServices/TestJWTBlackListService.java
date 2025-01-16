package ru.netology.CloudStorage_SpringBoot.testServices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.netology.CloudStorage_SpringBoot.entities.JWTBlackListEntity;
import ru.netology.CloudStorage_SpringBoot.repositories.JWTBlackListRepository;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUtils;

import java.util.Date;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.MockitoAnnotations.openMocks;

public class TestJWTBlackListService {

    @InjectMocks
    private JWTBlackListService jwtBlackListService;

    @Mock
    private JWTBlackListEntity jwtBlackListEntity;

    @Mock
    private JWTBlackListRepository jwtBlackListRepository;

    @Mock
    private JWTUtils utils;

    @BeforeEach
    void setUp() {
        openMocks(this);
        jwtBlackListEntity = new JWTBlackListEntity("a", 1L);
    }

    @Test
    void saveInBlackList() {
        when(utils.extractExpiration(anyString())).thenReturn(new Date());
        when(jwtBlackListRepository.saveAndFlush(any(JWTBlackListEntity.class)))
                .thenReturn(jwtBlackListEntity);
        jwtBlackListService.saveInBlackList(anyString());

        verify(utils, times(1)).extractExpiration(anyString());
        verify(jwtBlackListRepository, times(1)).saveAndFlush(any(JWTBlackListEntity.class));
    }

    @Test
    void isBlacklisted() {
        when(jwtBlackListRepository.findByJWTEquals(anyString())).thenReturn(Optional.of(jwtBlackListEntity));

        jwtBlackListService.isInBlackList(anyString());

        verify(jwtBlackListRepository, times(1)).findByJWTEquals(anyString());
    }
}
