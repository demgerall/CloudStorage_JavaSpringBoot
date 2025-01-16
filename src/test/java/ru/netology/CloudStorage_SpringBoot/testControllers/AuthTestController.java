package ru.netology.CloudStorage_SpringBoot.testControllers;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import ru.netology.CloudStorage_SpringBoot.dtos.requests.SignUpRequest;
import ru.netology.CloudStorage_SpringBoot.entities.Role;
import ru.netology.CloudStorage_SpringBoot.entities.User;
import ru.netology.CloudStorage_SpringBoot.security.UserDetailsService;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTAuthenticationEntryPoint;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTTokenFilter;
import ru.netology.CloudStorage_SpringBoot.security.jwt_security.JWTUtils;
import ru.netology.CloudStorage_SpringBoot.testServices.UserService;
import ru.netology.CloudStorage_SpringBoot.utils.mappers.UserMapper;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//

@SpringBootTest
@AutoConfigureMockMvc
public class AuthTestController {

    @InjectMocks
    private AuthController authController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JWTTokenFilter jwtTokenFilter;

    @MockitoBean
    private UserService userService;

    @MockitoBean(name = "userDetailsService")
    private UserDetailsService userDetailsService;

    @MockitoBean
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @MockitoBean
    private JWTUtils jwtUtils;

    @MockitoBean
    private UserMapper userMapper;

    @Test
    @DisplayName("Test signUp endpoint returns 200 OK")
    public void saveUser() throws Exception {
        User user = new User("user", "user", Role.ROLE_USER);
        user.setId(1L);
        SignUpRequest signupRequest = new SignUpRequest("user", "user");
        when(userMapper.convertToSignupUser(signupRequest)).thenReturn(user);
        doNothing().when(userService).registration(user);
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(signupRequest);
        mockMvc.perform(
                        post("/signup")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Test logIn endpoint returns 200 OK")
    void login() throws Exception {
        when(jwtUtils.generateJWTToken("user")).thenReturn("Bearer yourjwt");

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"login\": \"user\", \"password\": \"user\"}"))

                .andDo(print()).andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    @DisplayName(" Test logOut endpoint returns successful redirection to auth_login")
    void logout() throws Exception {

        mockMvc.perform(post("/logout")
                        .header("auth-token", "Bearer jwt"))
                .andDo(print())
                .andExpect(redirectedUrl("/login"));

    }
}
