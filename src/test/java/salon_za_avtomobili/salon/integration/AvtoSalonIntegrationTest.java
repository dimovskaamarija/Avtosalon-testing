package salon_za_avtomobili.salon.integration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.model.AvtoSalon;
import salon_za_avtomobili.salon.service.AvtoSalonService;
import salon_za_avtomobili.salon.utils.BaseTestData;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AvtoSalonIntegrationTest extends BaseTestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AvtoSalonService avtoSalonService;

    private AvtoSalon avtoSalon;

    @BeforeEach
    public void setUp() throws Exception {
        avtoSalon = getAvtoSalon();
        avtoSalon = avtoSalonService.save(avtoSalon.getName(),avtoSalon.getGrad(),avtoSalon.getLokacija(),avtoSalon.getKapacitet());
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetSalonPage() throws Exception {
        mockMvc.perform(get("/salon/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("salonList"))
                .andExpect(view().name("salonPage"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testShowFormForAdd() throws Exception {
        mockMvc.perform(get("/salon/addsalon"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("salon"))
                .andExpect(view().name("add-salon"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testSaveSalon() throws Exception {
        mockMvc.perform(post("/salon/add")
                        .param("name", "New Salon")
                        .param("grad", "New City")
                        .param("lokacija", "New Address")
                        .param("kapacitet", "200"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/salon/list"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testEditSalon() throws Exception {
        mockMvc.perform(get("/salon/edit-form/" + avtoSalon.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("salon"))
                .andExpect(view().name("add-salon"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testDeleteSalon() throws Exception {
        mockMvc.perform(delete("/salon/delete/" + avtoSalon.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/salon/list"));
    }
}
