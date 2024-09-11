package salon_za_avtomobili.salon.unit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import salon_za_avtomobili.salon.model.AvtoSalon;
import salon_za_avtomobili.salon.model.Avtomobil;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.repository.AvtomobilRep;
import salon_za_avtomobili.salon.service.AvtoSalonService;
import salon_za_avtomobili.salon.service.AvtomobilService;
import salon_za_avtomobili.salon.service.impl.AvtomobilServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;
@ExtendWith(SpringExtension.class)
class AvtomobilServiceTest extends BaseTestData {
    @Mock
    AvtomobilRep avtomobilRepository;
    @Mock
    AvtomobilService avtomobilService;
    @Mock
    AvtoSalonService avtoSalonService;
    @BeforeEach
    void setup() {
        avtomobilService=new AvtomobilServiceImpl(avtomobilRepository,avtoSalonService);
    }
    @Test
    void listAllWithSearch() {
        Avtomobil avtomobil=getAvtomobil();
        when(avtomobilRepository.findAllByNameContaining(avtomobil.getName())).thenReturn(Stream.of(avtomobil).collect(Collectors.toList()));
        List<Avtomobil> avtomobili=avtomobilService.listAllByIme(avtomobil.getName());
        assertThat(avtomobili.size()).isEqualTo(1);
    }
    @Test
    void listWithoutSearch() {
        Avtomobil avtomobil=getAvtomobil();
        when(avtomobilRepository.findAll()).thenReturn(Stream.of(avtomobil).collect(Collectors.toList()));
        List<Avtomobil> avtomobili=avtomobilService.listAll(null);
        assertThat(avtomobili.size()).isEqualTo(1);
    }
    @Test
    void findById() {
        Avtomobil avtomobil=getAvtomobil();
        when(avtomobilRepository.findById(avtomobil.getId())).thenReturn(Optional.of(avtomobil));
        Avtomobil avtomobil1 = avtomobilService.findById(avtomobil.getId()).get();
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
    void deleteById() {
        Avtomobil avtomobil = getAvtomobil();
        doNothing().when(avtomobilRepository).deleteById(avtomobil.getId());
        avtomobilService.deleteById(avtomobil.getId());
        verify(avtomobilRepository, times(1)).deleteById(avtomobil.getId());
    }
    @Test
    void update() {
        Avtomobil avtomobil = getAvtomobil();
        Avtomobil updatedCar = getUpdateAvtomobil();
        AvtoSalon avtoSalon = getUpdateAvtoSalon();
        List<AvtoSalon> avtoSaloni = new ArrayList<>();
        avtoSaloni.add(avtoSalon);
        avtomobil.setAvtoSaloni(avtoSaloni);
        when(avtoSalonService.findById(avtoSalon.getId())).thenReturn(Optional.of(avtoSalon));
        when(avtomobilRepository.findById(avtomobil.getId())).thenReturn(Optional.of(avtomobil));
        when(avtomobilRepository.save(any(Avtomobil.class))).thenReturn(avtomobil);
        avtomobilService.update(avtomobil.getId(), updatedCar.getName(), updatedCar.getPrice(), updatedCar.getYear(),
                updatedCar.getHorsepower(), updatedCar.getImage(), avtoSalon.getId());
        assertThat(avtomobil.getName()).isEqualTo(updatedCar.getName());
        assertThat(avtomobil.getYear()).isEqualTo(updatedCar.getYear());
        assertThat(avtomobil.getHorsepower()).isEqualTo(updatedCar.getHorsepower());
        assertThat(avtomobil.getImage()).isEqualTo(updatedCar.getImage());
        assertThat(avtomobil.getAvtoSaloni()).isEqualTo(updatedCar.getAvtoSaloni());
    }
    @Test
    void listCarsFromCarRentalShop() {
        Avtomobil avtomobil = getAvtomobil();
        List<Avtomobil> avtomobili = new ArrayList<>();
        avtomobili.add(avtomobil);
        AvtoSalon avtoSalon = getAvtoSalon();
        when(avtoSalonService.findById(avtoSalon.getId())).thenReturn(Optional.of(avtoSalon));
        when(avtomobilRepository.findByAvtoSaloniContaining(avtoSalon)).thenReturn(avtomobili);
        List<Avtomobil> avtomobili1 = avtomobilService.listFromSaloon(avtoSalon.getId());
        assertThat(avtomobili1.size()).isEqualTo(avtomobili.size());
    }
    @Test
    void shouldThrowCarNotFoundException() {
        Avtomobil avtomobil = getAvtomobil();
        AvtoSalon avtoSalon = getAvtoSalon();
        when(avtomobilRepository.findById(avtomobil.getId())).thenReturn(Optional.empty());
        assertThrows(
                InvalidArgumentException.class,
                () -> avtomobilService.update(avtomobil.getId(), avtomobil.getName(), avtomobil.getPrice(), avtomobil.getYear(), avtomobil.getHorsepower(),
                        avtomobil.getImage(), avtoSalon.getId()));
    }
}