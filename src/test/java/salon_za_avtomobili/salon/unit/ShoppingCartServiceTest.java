package salon_za_avtomobili.salon.unit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import salon_za_avtomobili.salon.model.Avtomobil;
import salon_za_avtomobili.salon.model.Korisnik;
import salon_za_avtomobili.salon.model.ShoppingCart;
import salon_za_avtomobili.salon.model.ShoppingCartStatus;
import salon_za_avtomobili.salon.repository.KorisnikRep;
import salon_za_avtomobili.salon.repository.ShoppingCartRep;
import salon_za_avtomobili.salon.service.AvtomobilService;
import salon_za_avtomobili.salon.service.ShoppingCartService;
import salon_za_avtomobili.salon.service.impl.ShoppingCartServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;
@ExtendWith(SpringExtension.class)
class ShoppingCartServiceTest extends BaseTestData {
    @Mock
    AvtomobilService avtomobilService;
    @Mock
    KorisnikRep korisnikRep;
    @Mock
    ShoppingCartRep shoppingCartRep;
    ShoppingCartService shoppingCartService;
    @BeforeEach
    void setup() {
        shoppingCartService = new ShoppingCartServiceImpl(shoppingCartRep, avtomobilService, korisnikRep);
    }
    @Test
    void listAllProducts() {
        ShoppingCart shoppingCart = getShoppingCart();
        Avtomobil avtomobil = shoppingCart.getAvtomobil();
        when(shoppingCartRep.findById(shoppingCart.getId())).thenReturn(Optional.of(shoppingCart));
        Avtomobil avtomobil1 = shoppingCartService.listAllProducts(shoppingCart.getId());
        assertThat(avtomobil1).isNotNull();
        assertThat(avtomobil1.getId()).isEqualTo(avtomobil.getId());
        assertThat(avtomobil1.getName()).isEqualTo(avtomobil.getName());
        assertThat(avtomobil1.getHorsepower()).isEqualTo(avtomobil.getHorsepower());
        assertThat(avtomobil1.getPrice()).isEqualTo(avtomobil.getPrice());
        assertThat(avtomobil1.getYear()).isEqualTo(avtomobil.getYear());
        assertThat(avtomobil1.getImage()).isEqualTo(avtomobil.getImage());
        assertThat(avtomobil1.getAvtoSaloni()).isEqualTo(avtomobil.getAvtoSaloni());
    }
    @Test
    void getActiveShoppingCart() {
        ShoppingCart shoppingCart = getShoppingCart();
        Korisnik korisnik = shoppingCart.getKorisnik();
        String username = korisnik.getUsername();
        when(korisnikRep.findByUsername(username)).thenReturn(Optional.of(korisnik));
        when(shoppingCartRep.findByUsernameAndStatus(username, ShoppingCartStatus.CREATED))
                .thenReturn(Optional.of(shoppingCart));
        ShoppingCart activeCart = shoppingCartService.getActiveShoppingCart(username);
        assertThat(activeCart).isNotNull();
        assertThat(activeCart.getId()).isEqualTo(shoppingCart.getId());
        assertThat(activeCart.getDateCreated()).isEqualTo(shoppingCart.getDateCreated());
        assertThat(activeCart.getAvtomobil()).isEqualTo(shoppingCart.getAvtomobil());
        assertThat(activeCart.getKorisnik()).isEqualTo(shoppingCart.getKorisnik());
    }
    @Test
    void addProductToShoppingCart() {
        ShoppingCart shoppingCart = getShoppingCart();
        Avtomobil avtomobil = shoppingCart.getAvtomobil();
        String username = shoppingCart.getKorisnik().getUsername();
        when(korisnikRep.findByUsername(username)).thenReturn(Optional.of(shoppingCart.getKorisnik()));
        when(shoppingCartRep.findByUsernameAndStatus(username, ShoppingCartStatus.CREATED))
                .thenReturn(Optional.of(shoppingCart));
        when(avtomobilService.findById(avtomobil.getId())).thenReturn(Optional.of(avtomobil));
        when(shoppingCartRep.save(any(ShoppingCart.class))).thenReturn(shoppingCart);
        ShoppingCart avtomobil1 = shoppingCartService.addProductToShoppingCart(username, avtomobil.getId());
        assertThat(avtomobil1).isNotNull();
        assertThat(avtomobil1.getId()).isEqualTo(shoppingCart.getId());
        assertThat(avtomobil1.getDateCreated()).isEqualTo(shoppingCart.getDateCreated());
        assertThat(avtomobil1.getAvtomobil()).isEqualTo(shoppingCart.getAvtomobil());
        assertThat(avtomobil1.getKorisnik()).isEqualTo(shoppingCart.getKorisnik());
    }
    @Test
    void findById() {
        ShoppingCart shoppingCart = getShoppingCart();
        Avtomobil avtomobil = shoppingCart.getAvtomobil();
        String username = shoppingCart.getKorisnik().getUsername();
        when(korisnikRep.findByUsername(username)).thenReturn(Optional.of(shoppingCart.getKorisnik()));
        when(shoppingCartRep.findByUsernameAndStatus(username, ShoppingCartStatus.CREATED))
                .thenReturn(Optional.of(shoppingCart));
        Optional<Avtomobil> avtomobil1 = shoppingCartService.findById(avtomobil.getId(), username);
        assertThat(avtomobil1).isPresent();
        assertThat(avtomobil1.get().getId()).isEqualTo(avtomobil.getId());
    }
}
