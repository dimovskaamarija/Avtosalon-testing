package salon_za_avtomobili.salon.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.Entity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
public class ShoppingCart {
    private Long id;
    private LocalDateTime dateCreated;
    private Korisnik korisnik;
    private Avtomobil avtomobil;
    private ShoppingCartStatus status;
    public ShoppingCart() {
        this.id = (long) (Math.random() * 1000);
    }
    public ShoppingCart(Korisnik korisnik, Avtomobil avtomobil) {
        this.id = (long) (Math.random() * 1000);
        this.dateCreated = LocalDateTime.now();
        this.korisnik = korisnik;
        this.avtomobil = avtomobil;
        this.status = ShoppingCartStatus.CREATED;
    }
    public ShoppingCart(Korisnik korisnik) {
        this.id = (long) (Math.random() * 1000);
        this.dateCreated = LocalDateTime.now();
        this.korisnik = korisnik;
        this.avtomobil = new Avtomobil();
        this.status = ShoppingCartStatus.CREATED;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalDateTime getDateCreated() {
        return dateCreated;
    }
    public void setDateCreated(LocalDateTime dateCreated) {
        this.dateCreated = dateCreated;
    }
    public Korisnik getKorisnik() {
        return korisnik;
    }
    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
    public Avtomobil getAvtomobil(Avtomobil avtomobil){
        return avtomobil;
    }
    public void setProducts(Avtomobil avtomobil) {
        this.avtomobil = avtomobil;
    }
    public ShoppingCartStatus getStatus() {
        return status;
    }
    public void setStatus(ShoppingCartStatus status) {
        this.status = status;
    }
}