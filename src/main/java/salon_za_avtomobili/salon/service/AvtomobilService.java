package salon_za_avtomobili.salon.service;
import salon_za_avtomobili.salon.model.Avtomobil;
import java.util.List;
import java.util.Optional;
public interface AvtomobilService {
    List<Avtomobil> listAll(Object o);
    Optional<Avtomobil> findById(Long id);
    Avtomobil save(String name, Integer price, Integer year, String horsepower, String image,Long salonId);
    void deleteById(Long id);
    void update(Long id,String name, Integer price, Integer year, String horsepower, String image, Long salon);
    List<Avtomobil> listFromSaloon(Long id);
    List<Avtomobil> listAllByIme(String name);

}
