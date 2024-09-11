package salon_za_avtomobili.salon.integration;

import org.apache.catalina.security.SecurityConfig;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.config.CustomUsernamePasswordAuthenticationProvider;
import salon_za_avtomobili.salon.model.User;
import salon_za_avtomobili.salon.service.UserService;
import salon_za_avtomobili.salon.web.UserController;

import java.util.Arrays;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@Import(SecurityConfig.class)
public class UserIntegrationTest {
        @MockBean
        private UserService userService;

        @MockBean
        private CustomUsernamePasswordAuthenticationProvider customAuthenticationProvider;

        @Autowired
        private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testGetUsersPage() throws Exception {
        User user1 = new User( "Marija", "Dimovska", "mare.d@example.com");
        User user2 = new User( "Darija", "Dimovska", "darija.d@example.com");
        Mockito.when(userService.listAll()).thenReturn(Arrays.asList(user1, user2));

        mockMvc.perform(get("/users/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("usersPage"))
                .andExpect(model().attributeExists("usersList"))
                .andExpect(model().attribute("usersList", Arrays.asList(user1, user2)));

        Mockito.verify(userService, Mockito.times(1)).listAll();
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteUser() throws Exception {
        Long userId = 1L;
        Mockito.doNothing().when(userService).deleteById(userId);

        mockMvc.perform(delete("/users/delete/{id}", userId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/list"));

        Mockito.verify(userService, Mockito.times(1)).deleteById(userId);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShowFormForAdd() throws Exception {
        mockMvc.perform(get("/users/adduser"))
                .andExpect(status().isOk())
                .andExpect(view().name("add-form"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testEditProductPage() throws Exception {
        User user = new User( "Marija", "Dimovska", "marija.d@example.com");
        Mockito.when(userService.findById(user.getId())).thenReturn(Optional.of(user));

        mockMvc.perform(get("/users/edit-form/{id}", user.getId()))
                .andExpect(status().isOk())
                .andExpect(view().name("add-form"))
                .andExpect(model().attributeExists("user"))
                .andExpect(model().attribute("user", user));

        Mockito.verify(userService, Mockito.times(1)).deleteById(user.getId());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testSaveUser() throws Exception {
        mockMvc.perform(post("/users/add")
                        .param("name", "Marija")
                        .param("surname", "Dimovska")
                        .param("email", "marija.d@example.com"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/users/list"));

        Mockito.verify(userService, Mockito.times(1))
                .save("Marija", "Dimovska", "marija.d@example.com");
    }
}
