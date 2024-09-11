package salon_za_avtomobili.salon.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import salon_za_avtomobili.salon.model.Avtomobil;
import salon_za_avtomobili.salon.model.ShoppingCart;
import salon_za_avtomobili.salon.service.AvtomobilService;
import salon_za_avtomobili.salon.service.ShoppingCartService;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ShoppingCartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShoppingCartService shoppingCartService;

    @MockBean
    private AvtomobilService avtomobilService;

    private ShoppingCart shoppingCart;
    private Avtomobil avtomobil;

    @BeforeEach
    void setUp() {
        shoppingCart = new ShoppingCart();
        avtomobil = new Avtomobil();
    }

    @Test
    @WithMockUser(username = "testuser", roles = {"ADMIN"})
    public void testGetShoppingCartPage() throws Exception {
        when(shoppingCartService.getActiveShoppingCart("testuser")).thenReturn(shoppingCart);
        when(shoppingCartService.listAllProducts(shoppingCart.getId())).thenReturn(avtomobil);

        mockMvc.perform(get("/shopping-cart"))
                .andExpect(status().isOk())
                .andExpect(view().name("shopping-cart"))
                .andExpect(model().attributeExists("product"));
    }

    @Test
    @WithMockUser(username = "admin")
    public void testAddProductToShoppingCart() throws Exception {
        when(avtomobilService.findById(1L)).thenReturn(Optional.of(avtomobil));
        when(shoppingCartService.getActiveShoppingCart("testuser")).thenReturn(shoppingCart);

        mockMvc.perform(post("/shopping-cart/add-product/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/shopping-cart"));
    }
}
