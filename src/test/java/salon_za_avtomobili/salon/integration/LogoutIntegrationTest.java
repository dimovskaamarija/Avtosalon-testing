package salon_za_avtomobili.salon.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.config.CustomUsernamePasswordAuthenticationProvider;
import salon_za_avtomobili.salon.web.LogoutController;

import javax.servlet.http.HttpSession;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LogoutController.class)
public class LogoutIntegrationTest {
    @MockBean
    private CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLogout() throws Exception {
        mockMvc.perform(get("/logout"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"))
                .andExpect(request().sessionAttributeDoesNotExist("korisnik"));
    }
}
