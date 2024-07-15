import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalService;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTest {

    @Test
    void checkBloodPressureTest() {

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("1", "Алексей", "Алексеевич",
                        LocalDate.of(2001,1,13),
                                new HealthInfo(new BigDecimal(40),new BloodPressure(120,80))));

        SendAlertService sendalertservice = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalserviceimpl= new MedicalServiceImpl(patientInfoFileRepository,sendalertservice);
        medicalserviceimpl.checkBloodPressure("1", new BloodPressure(140,90));

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendalertservice).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 1, need help",argumentCaptor.getValue());
    }

    @Test
    void checkTemperatureTest() {

        PatientInfoFileRepository patientInfoFileRepository = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepository.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("1", "Алексей", "Алексеевич",
                        LocalDate.of(2001,1,13),
                        new HealthInfo(new BigDecimal(38),new BloodPressure(120,80))));

        SendAlertService sendalertservice = Mockito.mock(SendAlertServiceImpl.class);

        MedicalServiceImpl medicalserviceimpl= new MedicalServiceImpl(patientInfoFileRepository,sendalertservice);
        medicalserviceimpl.checkTemperature("1", new BigDecimal("33.00")  );

        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        Mockito.verify(sendalertservice).send(argumentCaptor.capture());

        Assertions.assertEquals("Warning, patient with id: 1, need help",argumentCaptor.getValue());
    }
    @Test
    void indicators_are_normal() {

        PatientInfoFileRepository patientInfoFileRepositoryMock = Mockito.mock(PatientInfoFileRepository.class);
        Mockito.when(patientInfoFileRepositoryMock.getById((Mockito.anyString())))
                .thenReturn(new PatientInfo("1", "Алексей", "Алексеевич",
                        LocalDate.of(2001,1,13),
                        new HealthInfo(new BigDecimal(37), new BloodPressure(120, 80))));

        SendAlertService alertServiceMock = Mockito.mock(SendAlertServiceImpl.class);

        MedicalService medicalService = new MedicalServiceImpl(patientInfoFileRepositoryMock, alertServiceMock);

        medicalService.checkTemperature("1", new BigDecimal("36.00"));
        medicalService.checkBloodPressure("1", new BloodPressure(120, 80));

        Mockito.verify(alertServiceMock, Mockito.times(0))
                .send(Mockito.anyString());

    }
}