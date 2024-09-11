package salon_za_avtomobili.salon.utils;
import salon_za_avtomobili.salon.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import static salon_za_avtomobili.salon.model.Role.ROLE_ADMIN;
public class BaseTestData {
    protected Avtomobil getAvtomobil() {
        List<AvtoSalon> AvtoSaloni = new ArrayList<>();
        AvtoSalon AvtoSalon = getAvtoSalon();
        AvtoSaloni.add(AvtoSalon);
        return Avtomobil.builder()
                .name("name")
                .price(123)
                .year(2000)
                .horsepower("145")
                .image("image")
                .avtoSaloni(AvtoSaloni)
                .build();
    }

    protected Avtomobil getUpdateAvtomobil() {
        List<AvtoSalon> AvtoSaloni = new ArrayList<>();
        AvtoSalon AvtoSalon = getUpdateAvtoSalon();
        AvtoSaloni.add(AvtoSalon);
        return Avtomobil.builder()
                .id(1L)
                .name("update-name")
                .price(1234)
                .year(2001)
                .horsepower("146")
                .image("update-image")
                .avtoSaloni(AvtoSaloni)
                .build();
    }
    protected AvtoSalon getAvtoSalon() {
        return AvtoSalon.builder()
                .name("name")
                .grad("city")
                .lokacija("address")
                .build();
    }
    protected AvtoSalon getUpdateAvtoSalon() {
        return AvtoSalon.builder()
                .name("update-name")
                .grad("update-city")
                .lokacija("update-address")
                .build();
    }
    protected User getUser() {
        return User.builder()
                .name("name")
                .surname("lastName")
                .password("password")
                .email("email@outlook.com")
                .id(1L)
                .build();
    }
    protected Korisnik getKorisnik(){
        return Korisnik.builder()
                .name("name")
                .surname("surname")
                .username("admin")
                .password("pw").
                role(ROLE_ADMIN).
                build();
    }
    protected ShoppingCart getShoppingCart() {
     Korisnik korisnik=getKorisnik();
        Avtomobil avtomobil = getAvtomobil();
        return ShoppingCart.builder()
                .id(1L)
                .dateCreated(LocalDateTime.parse("2022-03-20T00:00:00"))
                .avtomobil(avtomobil)
                .status(ShoppingCartStatus.CREATED)
                .korisnik(korisnik)
                .build();
    }
}