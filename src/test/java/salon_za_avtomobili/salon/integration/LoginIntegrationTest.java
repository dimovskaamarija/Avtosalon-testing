
package salon_za_avtomobili.salon.integration;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.config.CustomUsernamePasswordAuthenticationProvider;
import salon_za_avtomobili.salon.model.Korisnik;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.service.KorisnikService;
import salon_za_avtomobili.salon.web.LoginController;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LoginIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KorisnikService korisnikService;

    @MockBean
    private CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;

    @Test
    public void testGetLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    public void testLoginSuccess() throws Exception {
        Korisnik korisnik = new Korisnik();
        Mockito.when(korisnikService.login("validUsername", "validPassword"))
                .thenReturn(korisnik);

        mockMvc.perform(post("/login")
                        .param("username", "validUsername")
                        .param("password", "validPassword"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        Mockito.verify(korisnikService, Mockito.times(1))
                .login("validUsername", "validPassword");
    }

    @Test
    public void testLoginInvalidCredentials() throws Exception {
        Mockito.when(korisnikService.login("invalidUsername", "invalidPassword"))
                .thenThrow(new InvalidArgumentException());

        mockMvc.perform(post("/login")
                        .param("username", "invalidUsername")
                        .param("password", "invalidPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("hasError", true))
                .andExpect(model().attribute("error", "Vnesete argumenti"));

        Mockito.verify(korisnikService, Mockito.times(1))
                .login("invalidUsername", "invalidPassword");
    }
}
