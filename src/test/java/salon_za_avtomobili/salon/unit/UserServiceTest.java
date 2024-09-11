package salon_za_avtomobili.salon.unit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import salon_za_avtomobili.salon.model.User;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.repository.UserRep;
import salon_za_avtomobili.salon.service.impl.UserServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class UserServiceTest extends BaseTestData {
    @Mock
    private UserRep userRepository;
    @InjectMocks
    private UserServiceImpl userService;
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    public void testListAll() {
        User user = getUser();
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> result = userService.listAll();
        assertEquals(1, result.size());
        assertEquals(user, result.get(0));
    }
    @Test
    public void testFindByIdSuccess() {
        User user = getUser();
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        Optional<User> result = userService.findById(user.getId());
        assertTrue(result.isPresent());
        assertEquals(user, result.get());
    }
    @Test
    public void testFindByIdNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Optional<User> result = userService.findById(1L);
        assertFalse(result.isPresent());
    }
    @Test
    public void testSaveSuccess() {
        User user = getUser();
        when(userRepository.save(any(User.class))).thenReturn(user);
        User result = userService.save(user.getName(), user.getSurname(), user.getEmail());
        assertNotNull(result);
        assertEquals(user.getName(), result.getName());
        assertEquals(user.getSurname(), result.getSurname());
        assertEquals(user.getEmail(), result.getEmail());
    }
    @Test
    public void testSaveInvalidArguments() {
        assertThrows(InvalidArgumentException.class, () -> userService.save(null, "surname", "email"));
        assertThrows(InvalidArgumentException.class, () -> userService.save("name", null, "email"));
        assertThrows(InvalidArgumentException.class, () -> userService.save("", "surname", "email"));
        assertThrows(InvalidArgumentException.class, () -> userService.save("name", "", "email"));
    }
    @Test
    public void testDeleteById() {
        doNothing().when(userRepository).deleteById(1L);
        userService.deleteById(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }
}
