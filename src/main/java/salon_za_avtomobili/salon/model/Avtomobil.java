package salon_za_avtomobili.salon.model;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;
import java.util.List;
@AllArgsConstructor
@Data
@Entity
@Builder
public class Avtomobil {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer price;
    private Integer year;
    private String horsepower;
    private String image;
    private Integer klima=0;
    private Integer metalic=0;
    private Integer bandasi=0;
    private Integer turbo=0;
    private Integer spoiler=0;
    private Integer kozniSedista=0;
    @ManyToMany(fetch = FetchType.EAGER)
    private List<AvtoSalon> avtoSaloni;
    public Avtomobil() {
    }
    public Avtomobil(String name, Integer price, Integer year, String horsepower, String image) {
        this.name = name;
        this.year = year;
        this.horsepower = horsepower;
        this.image = image;
        this.price = price;
        this.avtoSaloni=new ArrayList<>();
    }
    public int getKlima() {
        return klima;
    }
    public void setKlima(int klima) {
        this.klima = klima;
    }
    public List<AvtoSalon> getAvtoSaloni() {
        return avtoSaloni;
    }
    public Integer getPrice() {
        return price;
    }
    public void setPrice(Integer price) {
        this.price = price;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public String getHorsepower() {
        return horsepower;
    }
    public void setHorsepower(String horsepower) {
        this.horsepower = horsepower;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public void setKlima(Integer klima) {
        this.klima = klima;
    }
    public Integer getMetalic() {
        return metalic;
    }
    public void setMetalic(Integer metalic) {
        this.metalic = metalic;
    }
    public Integer getBandasi() {
        return bandasi;
    }
    public void setBandasi(Integer bandasi) {
        this.bandasi = bandasi;
    }
    public Integer getTurbo() {
        return turbo;
    }
    public void setTurbo(Integer turbo) {
        this.turbo = turbo;
    }
    public Integer getSpoiler() {
        return spoiler;
    }
    public void setSpoiler(Integer spoiler) {
        this.spoiler = spoiler;
    }
    public Integer getKozniSedista() {
        return kozniSedista;
    }
    public void setKozniSedista(Integer kozniSedista) {
        this.kozniSedista = kozniSedista;
    }
    public void setAvtoSaloni(List<AvtoSalon> avtoSaloni) {
        this.avtoSaloni = avtoSaloni;
    }
    public String calcPrice() {
       Integer ret=price+getKlima()+getMetalic()+getTurbo()+getSpoiler()+getKozniSedista()+getBandasi();
       return  ret.toString();
    }
}
