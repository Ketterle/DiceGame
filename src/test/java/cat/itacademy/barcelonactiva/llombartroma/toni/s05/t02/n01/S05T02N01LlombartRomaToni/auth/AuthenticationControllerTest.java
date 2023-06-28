package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class AuthenticationControllerTest {

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private AuthenticationController authenticationController;

    public AuthenticationControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerSuccessTest() {
        // Mock the behavior of the authentication service
        RegisterRequest request = new RegisterRequest();
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token");
        when(authenticationService.register(any(RegisterRequest.class))).thenReturn(expectedResponse);

        // Call the register method of the controller
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.register(request);
        AuthenticationResponse response = responseEntity.getBody();

        // Verify the response
        assertEquals(expectedResponse, response);
    }

    @Test
    void authenticateSuccessTest() throws PlayerNotFoundException {
        // Mock the behavior of the authentication service
        AuthenticationRequest request = new AuthenticationRequest();
        AuthenticationResponse expectedResponse = new AuthenticationResponse("token");
        when(authenticationService.authenticate(any(AuthenticationRequest.class))).thenReturn(expectedResponse);

        // Call the authenticate method of the controller
        ResponseEntity<AuthenticationResponse> responseEntity = authenticationController.authenticate(request);
        AuthenticationResponse response = responseEntity.getBody();

        // Verify the response
        assertEquals(expectedResponse, response);
    }
}


