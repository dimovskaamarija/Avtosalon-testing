package salon_za_avtomobili.salon.unit;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import salon_za_avtomobili.salon.model.AvtoSalon;
import salon_za_avtomobili.salon.model.excep.InvalidArgumentException;
import salon_za_avtomobili.salon.repository.AvtoSalonRep;
import salon_za_avtomobili.salon.service.AvtoSalonService;
import salon_za_avtomobili.salon.service.impl.AvtoSalonServiceImpl;
import salon_za_avtomobili.salon.utils.BaseTestData;
@ExtendWith(MockitoExtension.class)
class AvtoSalonServiceTest extends BaseTestData {
    @Mock
    AvtoSalonRep avtoSalonRep;
    AvtoSalonService avtoSalonService;
    @BeforeEach
    void setup() {
        avtoSalonService = new AvtoSalonServiceImpl(avtoSalonRep);
    }
    @Test
    void listAll() {
        AvtoSalon avtoSalon = getAvtoSalon();
        when(avtoSalonRep.findAll()).thenReturn(Stream.of(avtoSalon).collect(Collectors.toList()));
        List<AvtoSalon> avtoSaloni = avtoSalonService.listAll();
        assertThat(avtoSaloni.size()).isEqualTo(1);
    }
    @Test
    void findById() {
        AvtoSalon avtoSalon = getAvtoSalon();
        when(avtoSalonRep.findById(avtoSalon.getId())).thenReturn(Optional.of(avtoSalon));
        AvtoSalon avtoSalon1 = avtoSalonService.findById(avtoSalon.getId()).get();
        assertThat(avtoSalon1).isNotNull();
        assertThat(avtoSalon1.getId()).isEqualTo(avtoSalon.getId());
        assertThat(avtoSalon1.getName()).isEqualTo(avtoSalon.getName());
        assertThat(avtoSalon1.getLokacija()).isEqualTo(avtoSalon.getLokacija());
        assertThat(avtoSalon1.getGrad()).isEqualTo(avtoSalon.getGrad());
    }
    @Test
    void addAvtoSalon() {
        AvtoSalon avtoSalon = getAvtoSalon();
        when(avtoSalonRep.save(any(AvtoSalon.class))).thenReturn(avtoSalon);
        AvtoSalon avtoSalon1 = avtoSalonService.save(avtoSalon.getName(), avtoSalon.getGrad(),
                avtoSalon.getLokacija(), avtoSalon.getKapacitet());
        assertThat(avtoSalon1).isNotNull();
        assertThat(avtoSalon1.getName()).isEqualTo(avtoSalon.getName());
        assertThat(avtoSalon1.getGrad()).isEqualTo(avtoSalon.getGrad());
        assertThat(avtoSalon1.getLokacija()).isEqualTo(avtoSalon.getLokacija());
        assertThat(avtoSalon1.getKapacitet()).isEqualTo(avtoSalon.getKapacitet());
    }
    @Test
    void deleteById() {
        AvtoSalon avtoSalon = getAvtoSalon();
        doNothing().when(avtoSalonRep).deleteById(avtoSalon.getId());
        avtoSalonService.deleteById(avtoSalon.getId());
        verify(avtoSalonRep, times(1)).deleteById(avtoSalon.getId());
    }
}
