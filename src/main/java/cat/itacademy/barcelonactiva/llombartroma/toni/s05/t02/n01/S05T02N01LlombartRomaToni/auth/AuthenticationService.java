package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.auth;

import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.config.JwtService;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.controllers.PlayerNotFoundException;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.Role;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain.User;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMongo;
import cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.repositories.UserRepositoryMySQL;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final UserRepositoryMongo userRepositoryMongo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;


    public AuthenticationResponse register(RegisterRequest request) {
        try {
            Optional<List<User>> playerRetrievedOptional = userRepositoryMySQL.getUsersByName(request.getName());
            if (!playerRetrievedOptional.get().isEmpty() && playerRetrievedOptional.get().stream().noneMatch(s -> s.getName().equals((User.DEFAULT_NAME)))) {
                throw new PlayerAlreadyExistsException();
            } else {
                var player = User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .build();
                if(userRepositoryMongo.findAll().isEmpty() && player.getRole().equals(Role.ADMIN)) {
                    userRepositoryMongo.save(player);
                }
                else {
                    userRepositoryMySQL.save(player);
                }
                var jwtToken = jwtService.generateToken(player);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }
        }
        catch( Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws PlayerNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var player = userRepositoryMySQL.findByEmail(request.getEmail()).orElseThrow(()-> new PlayerNotFoundException());
        var jwtToken = jwtService.generateToken(player);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .build();
    }
}
