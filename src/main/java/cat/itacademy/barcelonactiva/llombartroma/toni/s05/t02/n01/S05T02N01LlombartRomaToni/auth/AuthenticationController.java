package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    private final AuthenticationService service;

    public AuthenticationController(AuthenticationService service) {
        this.service = service;
    }

    @PostMapping(EndpointConstantsAuthenticate.ADD_PLAYER)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @PostMapping(EndpointConstantsAuthenticate.AUTHENTICATE_PLAYER)
    public ResponseEntity<AuthenticationResponse> register(@RequestBody AuthenticationRequest request) throws PlayerNotFoundException {
        return ResponseEntity.ok(service.authenticate(request));
    }

    public static class EndpointConstantsAuthenticate {
        public static final String ADD_PLAYER = "/dicegame/player/auth/register";
        public static final String AUTHENTICATE_PLAYER = "/dicegame/player/auth/authenticate";
    }
}
