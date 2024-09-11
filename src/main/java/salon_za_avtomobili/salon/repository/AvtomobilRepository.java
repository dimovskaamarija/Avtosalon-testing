package salon_za_avtomobili.salon.repository;
import org.springframework.stereotype.Repository;
import salon_za_avtomobili.salon.bootstrap.DataHolder;
import salon_za_avtomobili.salon.model.Avtomobil;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Repository
public class AvtomobilRepository {
    public List<Avtomobil> listAll() {
        return DataHolder.AvtomobilList;
    }
    public Optional<Avtomobil> findById(Long id) {
        return DataHolder.AvtomobilList.stream().filter(i -> i.getId().equals(id)).findFirst();
    }
    public void deleteById(Long id) {
        DataHolder.AvtomobilList.removeIf(i -> i.getId().equals(id));
    }
    public Optional<Avtomobil> save(String name, Integer price, Integer year, String horsepower, String image
    ) {
        DataHolder.AvtomobilList.removeIf(i -> i.getName().equals(name));
        Avtomobil Avtomobil = new Avtomobil(name, price, year, horsepower, image);
        DataHolder.AvtomobilList.add(Avtomobil);
        return Optional.of(Avtomobil);
    }
}
