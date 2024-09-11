package salon_za_avtomobili.salon.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import salon_za_avtomobili.salon.model.Avtomobil;
import salon_za_avtomobili.salon.model.Korisnik;
import salon_za_avtomobili.salon.model.ShoppingCart;
import salon_za_avtomobili.salon.model.ShoppingCartStatus;
import salon_za_avtomobili.salon.repository.KorisnikRep;
import salon_za_avtomobili.salon.repository.ShoppingCartRep;
import salon_za_avtomobili.salon.service.AvtomobilService;
import salon_za_avtomobili.salon.service.ShoppingCartService;
import java.util.Optional;
@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRep shoppingCartRepository;
    private final AvtomobilService avtomobilService;
    private final KorisnikRep korisnikRepository;
    @Override
    public Avtomobil listAllProducts(Long cartId) {
        return this.shoppingCartRepository.findById(cartId).get().getAvtomobil();
    }
    @Override
    public ShoppingCart getActiveShoppingCart(String username) {
        return this.shoppingCartRepository
                .findByUsernameAndStatus(username, ShoppingCartStatus.CREATED)
                .orElseGet(() -> {
                    Korisnik korisnik = this.korisnikRepository.findByUsername(username)
                            .orElseThrow();
                    ShoppingCart shoppingCart = new ShoppingCart(korisnik);
                    return this.shoppingCartRepository.save(shoppingCart);
                });
    }
    @Override
    public ShoppingCart addProductToShoppingCart(String username, Long carId) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        Avtomobil avtomobil = this.avtomobilService.findById(carId)
                .orElseThrow();
        shoppingCart.setProducts(avtomobil);
        return this.shoppingCartRepository.save(shoppingCart);
    }
    @Override
    public Optional<Avtomobil> findById(Long id, String username) {
        ShoppingCart shoppingCart = this.getActiveShoppingCart(username);
        return Optional.of(shoppingCart.getAvtomobil());
    }
}
