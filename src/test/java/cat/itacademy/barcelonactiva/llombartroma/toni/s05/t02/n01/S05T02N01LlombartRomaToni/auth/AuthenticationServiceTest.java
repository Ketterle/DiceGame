package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.config.JwtService;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Role;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMongo;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AuthenticationServiceTest {

    @Mock
    private UserRepositoryMySQL userRepositoryMySQL;

    @Mock
    private UserRepositoryMongo userRepositoryMongo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerSuccessTest() {
        // Mock the behavior of dependencies and repositories
        when(userRepositoryMySQL.getUsersByName(anyString())).thenReturn(Optional.empty());
        when(userRepositoryMongo.findAll()).thenReturn(Collections.emptyList());
        when(userRepositoryMongo.save(any(User.class))).thenReturn(new User());
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        // Create a RegisterRequest object with test data
        RegisterRequest request = new RegisterRequest();
        request.setName("John");
        request.setEmail("john@example.com");
        request.setPassword("password");
        request.setRole(Role.PLAYER);

        // Call the register method
        AuthenticationResponse response = authenticationService.register(request);

        // Verify the method invocations and assert the expected outcome
        verify(userRepositoryMySQL).getUsersByName(anyString());
        verify(userRepositoryMySQL).save(any(User.class));
        verify(jwtService).generateToken(any(User.class));

        assertEquals("token", response.getToken());
    }

    @Test
    void authenticateSuccessTest() {
        // Mock the behavior of dependencies and repositories
        User user = new User();
        when(userRepositoryMySQL.findByEmail(anyString())).thenReturn(Optional.of(user));
        when(jwtService.generateToken(any(User.class))).thenReturn("token");

        // Create an AuthenticationRequest object with test data
        AuthenticationRequest request = new AuthenticationRequest();
        request.setEmail("john@example.com");
        request.setPassword("password");
        request.setRole(Role.PLAYER);

        // Call the authenticate method
        AuthenticationResponse response;
        try {
            response = authenticationService.authenticate(request);
        } catch (PlayerNotFoundException e) {
            throw new RuntimeException(e);
        }

        // Verify the method invocations and assert the expected outcome
        verify(userRepositoryMySQL).findByEmail(anyString());
        verify(jwtService).generateToken(any(User.class));

        assertEquals("token", response.getToken());
    }
}
