package salon_za_avtomobili.salon.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import salon_za_avtomobili.salon.model.Korisnik;
import salon_za_avtomobili.salon.model.Role;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.repository.KorisnikRep;
import salon_za_avtomobili.salon.service.KorisnikService;
import salon_za_avtomobili.salon.service.impl.KorisnikServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class KorisnikServiceTest extends BaseTestData {
    @Mock
    private KorisnikRep korisnikRep;
    @Mock
    private PasswordEncoder passwordEncoder;
    @InjectMocks
    private KorisnikServiceImpl korisnikService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testLoginSuccess() {
        Korisnik korisnik = getKorisnik();
        when(korisnikRep.findByUsernameAndPassword(korisnik.getUsername(), korisnik.getPassword())).thenReturn(Optional.of(korisnik));
        Korisnik result = korisnikService.login(korisnik.getUsername(), korisnik.getPassword());
        assertEquals(korisnik, result);
    }
    @Test
    public void testLoginFailure() {
        Korisnik korisnik = getKorisnik();
        when(korisnikRep.findByUsernameAndPassword(korisnik.getUsername(), korisnik.getPassword())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> korisnikService.login(korisnik.getUsername(), korisnik.getPassword()));
    }
    @Test
    public void testRegisterSuccess() {
        Korisnik korisnik = getKorisnik();
        when(korisnikRep.findByUsername(korisnik.getUsername())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Korisnik newUser = new Korisnik("user", "encodedPassword", "Marija", "Dimovska", Role.ROLE_USER);
        when(korisnikRep.save(any(Korisnik.class))).thenReturn(newUser);
        Korisnik result = korisnikService.register("user", "password", "password", "Marija", "Dimovska", Role.ROLE_USER);
        assertEquals(newUser, result);
    }
    @Test
    public void testRegisterUsernameNull() {
        assertThrows(InvalidArgumentException.class, () -> korisnikService.register(null, "password", "password", "Marija", "Dimovska", Role.ROLE_USER));
    }
    @Test
    public void testRegisterPasswordNull() {
        assertThrows(InvalidArgumentException.class, () -> korisnikService.register("user", null, "password", "Marija", "Dimovska", Role.ROLE_USER));
    }
    @Test
    public void testRegisterPasswordMismatch() {
        assertThrows(InvalidArgumentException.class, () -> korisnikService.register("user", "password", "differentPassword", "Marija", "Dimovska", Role.ROLE_USER));
    }
    @Test
    public void testRegisterUsernameExists() {
        when(korisnikRep.findByUsername("user")).thenReturn(Optional.of(getKorisnik()));
        assertThrows(InvalidArgumentException.class, () -> korisnikService.register("user", "password", "password", "Marija", "Dimovska", Role.ROLE_USER));
    }
    @Test
    public void testLoadUserByUsernameSuccess() {
        Korisnik korisnik = getKorisnik();
        when(korisnikRep.findByUsername(korisnik.getUsername())).thenReturn(Optional.of(korisnik));
        UserDetails result = korisnikService.loadUserByUsername(korisnik.getUsername());
        assertEquals(korisnik, result);
    }
    @Test
    public void testLoadUserByUsernameFailure() {
        when(korisnikRep.findByUsername("user")).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> korisnikService.loadUserByUsername("user"));
    }
}
