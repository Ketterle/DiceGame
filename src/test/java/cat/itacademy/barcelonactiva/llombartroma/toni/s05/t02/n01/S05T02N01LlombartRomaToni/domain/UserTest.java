package cat.itacademy.barcelonactiva.llombartroma.toni.s05.t02.n01.S05T02N01LlombartRomaToni.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserTest {

    @Mock
    private Role role;

    @InjectMocks
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setRole(Role.PLAYER);
    }

    @Test
    public void getPasswordTest() {
        String password = "password";
        user.setPassword(password);

        String result = user.getPassword();

        assertEquals(password, result);
    }

    @Test
    public void getUsernameTest() {
        String email = "test@example.com";
        user.setEmail(email);

        String result = user.getUsername();

        assertEquals(email, result);
    }

    @Test
    public void isAccountNonExpiredTest() {
        boolean result = user.isAccountNonExpired();

        assertEquals(true, result);
    }

    @Test
    public void isAccountNonLockedTest() {
        boolean result = user.isAccountNonLocked();

        assertEquals(true, result);
    }

    @Test
    public void isCredentialsNonExpiredTest() {
        boolean result = user.isCredentialsNonExpired();

        assertEquals(true, result);
    }

    @Test
    public void isEnabledTest() {
        boolean result = user.isEnabled();

        assertEquals(true, result);
    }
}
