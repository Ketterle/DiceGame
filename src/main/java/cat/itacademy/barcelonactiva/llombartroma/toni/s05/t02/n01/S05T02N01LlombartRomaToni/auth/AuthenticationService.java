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

/* This class implements the business logic when adding or log in a user
* We could use the DiceGameServicesMySQL layer, but we prefer to split both logics
* since Mongo DB persistence is present as well.
 */
@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepositoryMySQL userRepositoryMySQL;
    private final UserRepositoryMongo userRepositoryMongo;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    /* This method registers a new user */
    public AuthenticationResponse register(RegisterRequest request) {
        try {
            /* We want to make sure no name or email is duplicated */
            Optional<List<User>> userRetrievedOptionalName = userRepositoryMySQL.getUsersByName(request.getName());if ((!userRetrievedOptionalName.isEmpty() && userRetrievedOptionalName.get().stream().noneMatch(s -> s.getName().equals(User.DEFAULT_NAME)))
                    || (request.getRole().equals(Role.ADMIN) && !userRepositoryMongo.findAll().isEmpty())
                    || (userRepositoryMySQL.findByEmail(request.getEmail()).isPresent())
                    || (userRepositoryMongo.findByEmail(request.getEmail()).isPresent())) {
                throw new PlayerAlreadyExistsException();
            }
            /* Admin role is persisted in Mongo DB, and we only allow a single Admin */
            else if(request.getRole().equals(Role.ADMIN)) {
                var admin = User.builder()
                        .name("ADMIN")
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .dateOfRegistration(java.time.LocalDateTime.now().toString())
                        .build();
                    userRepositoryMongo.save(admin);
                var jwtToken = jwtService.generateToken(admin);
                return AuthenticationResponse.builder()
                        .token(jwtToken)
                        .build();
            }
            else {
                /* Player role is persisted in MySQL */
                var player = User.builder()
                        .name(request.getName()==null?User.DEFAULT_NAME:request.getName())
                        .email(request.getEmail())
                        .password(passwordEncoder.encode(request.getPassword()))
                        .role(request.getRole())
                        .dateOfRegistration(java.time.LocalDateTime.now().toString())
                        .build();
                userRepositoryMySQL.save(player);
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

    /* This method allows to authenticate users, both Admin and Players */
    public AuthenticationResponse authenticate(AuthenticationRequest request) throws PlayerNotFoundException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        if (request.getRole().equals(Role.PLAYER)) {
            var player = userRepositoryMySQL.findByEmail(request.getEmail()).orElseThrow(PlayerNotFoundException::new);
            var jwtToken = jwtService.generateToken(player);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        } else {
            var admin = userRepositoryMongo.findByEmail(request.getEmail()).orElseThrow(PlayerNotFoundException::new);
            var jwtToken = jwtService.generateToken(admin);
            return AuthenticationResponse.builder()
                    .token(jwtToken)
                    .build();
        }
    }
}
