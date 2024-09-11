package salon_za_avtomobili.salon.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.event.annotation.BeforeTestMethod;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.model.Korisnik;
import salon_za_avtomobili.salon.model.Role;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.service.KorisnikService;
import salon_za_avtomobili.salon.service.impl.KorisnikServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class KorisnikIntegrationTest  {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    KorisnikService korisnikService;



    @Test
    public void testGetRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeDoesNotExist("hasError"))
                .andExpect(model().attributeDoesNotExist("error"));
    }

    @Test
    public void testSuccessfulRegistration() throws Exception {
        mockMvc.perform(post("/register")
                        .param("username", "admin")
                        .param("password", "Testpassword1!")
                        .param("repeatedPassword", "Testpassword1!")
                        .param("name", "Marija")
                        .param("surname", "Dimovska").param("role",Role.ROLE_USER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }




    @Test
    public void testRegistrationWithInvalidArguments() throws Exception {
        doThrow(new InvalidArgumentException())
                .when(korisnikService).register("nooo", "password", "differentpassword", "", "Dimovska", Role.ROLE_USER);
        mockMvc.perform(post("/register")
                        .param("username", "nooo")
                        .param("password", "password")
                        .param("repeatedPassword", "differentpassword")
                        .param("name", "")
                        .param("surname", "Dimovska")
                        .param("role", "ROLE_USER"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error=Vnesete argumenti"));
    }

    @Test
    public void testRegistrationWithMissingFields() throws Exception {
        doThrow(new InvalidArgumentException())
                .when(korisnikService).register("dimovska", "testpassword", "testpassword", "", "Dimovska", Role.ROLE_USER);

        mockMvc.perform(post("/register")
                        .param("username", "dimovska")
                        .param("password", "testpassword")
                        .param("repeatedPassword", "testpassword")
                        .param("name", "")
                        .param("surname", "Dimovska")
                        .param("role", Role.ROLE_USER.name()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/register?error=Vnesete argumenti"));
    }

}
