package salon_za_avtomobili.salon.integration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.model.Avtomobil;
import salon_za_avtomobili.salon.model.AvtoSalon;
import salon_za_avtomobili.salon.service.AvtoSalonService;
import salon_za_avtomobili.salon.service.AvtomobilService;
import salon_za_avtomobili.salon.utils.BaseTestData;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@SpringBootTest
@AutoConfigureMockMvc
public class AvtomobilIntegrationTest  extends BaseTestData{

    @Autowired
    private MockMvc mockMvc;

    private Avtomobil avtomobil;
    private AvtoSalon avtoSalon;

    @Autowired
    private AvtomobilService avtomobilService;

    @Autowired
    private AvtoSalonService avtoSalonService;

        @BeforeEach
        public void setUp() throws Exception {
            avtoSalon = getAvtoSalon();
            avtoSalon = avtoSalonService.save(avtoSalon.getName(),avtoSalon.getGrad(),avtoSalon.getLokacija(),avtoSalon.getKapacitet());
            avtomobil = getAvtomobil();
            if (avtomobil.getAvtoSaloni() == null) {
                avtomobil.setAvtoSaloni(new ArrayList<>());
            }
            avtomobil.getAvtoSaloni().add(avtoSalon);
            avtomobil = avtomobilService.save(avtomobil.getName(),avtomobil.getPrice(),avtomobil.getYear(),avtomobil.getHorsepower(),avtomobil.getImage(),avtoSalon.getId());


    }
    
    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void testGetCarsPage() throws Exception {
        mockMvc.perform(get("/cars/list"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("carsList"))
                .andExpect(view().name("carsPage"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testAddCar() throws Exception {
        mockMvc.perform(post("/cars/add")
                        .param("name", "Car 2")
                        .param("price", "15000")
                        .param("year", "2021")
                        .param("horsepower", "180hp")
                        .param("image", "image2.jpg")
                        .param("salon", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cars/list"));
    }


    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testEditCar() throws Exception {
        mockMvc.perform(get("/cars/edit-form/" + avtomobil.getId()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("car"))
                .andExpect(view().name("add-car"));
    }
    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void testDeleteCar() throws Exception {
        mockMvc.perform(delete("/cars/delete/" + avtomobil.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/cars/list"));
    }
}