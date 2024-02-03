package pxl.kwops.humanresources.boundary.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.boundary.models.EmployeeEntity;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

class EmployeeRepositoryAdapterImplTest {

    public static final String NUMBER = "20240101001";
    public static final EmployeeNumber EMPLOYEE_NUMBER = new EmployeeNumber(NUMBER);
    private static final Employee EMPLOYEE = Employee.builder()
            .number(EMPLOYEE_NUMBER)
            .firstName("John")
            .lastName("Doe")
            .build();
    private static final EmployeeEntity EMPLOYEE_ENTITY = EmployeeEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .build();
    private AutoCloseable closeable;
    @Mock
    private EmployeeJpaRepository employeeJpaRepository;
    @Mock
    private EmployeeMapper employeeMapper;
    @InjectMocks
    private EmployeeRepositoryAdapterImpl employeeRepositoryAdapter;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void addEmployee() {
        when(employeeMapper.toEmployeeEntity(EMPLOYEE)).thenReturn(EMPLOYEE_ENTITY);
        when(employeeJpaRepository.save(EMPLOYEE_ENTITY)).thenReturn(EMPLOYEE_ENTITY);
        when(employeeMapper.toEmployee(EMPLOYEE_ENTITY)).thenReturn(EMPLOYEE);

        var result = employeeRepositoryAdapter.addEmployee(EMPLOYEE);

        verify(employeeMapper).toEmployeeEntity(EMPLOYEE);
        verify(employeeJpaRepository).save(EMPLOYEE_ENTITY);
        verify(employeeMapper).toEmployee(EMPLOYEE_ENTITY);
        assertThat(result).isEqualTo(EMPLOYEE);
    }

    @Test
    void getEmployeeByNumber() {
        when(employeeMapper.mapFromEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(NUMBER);
        when(employeeJpaRepository.findEmployeeByNumber(NUMBER)).thenReturn(Optional.of(EMPLOYEE_ENTITY));
        when(employeeMapper.toEmployee(EMPLOYEE_ENTITY)).thenReturn(EMPLOYEE);

        var result = employeeRepositoryAdapter.getEmployeeByNumber(EMPLOYEE_NUMBER);

        verify(employeeMapper).mapFromEmployeeNumber(EMPLOYEE_NUMBER);
        verify(employeeJpaRepository).findEmployeeByNumber(NUMBER);
        verify(employeeMapper).toEmployee(EMPLOYEE_ENTITY);
        assertThat(result).contains(EMPLOYEE);
    }

    @Test
    void getEmployeeByNumber_notFound() {
        when(employeeMapper.mapFromEmployeeNumber(EMPLOYEE_NUMBER)).thenReturn(NUMBER);
        when(employeeJpaRepository.findEmployeeByNumber(NUMBER)).thenReturn(Optional.empty());
        when(employeeMapper.toEmployee(any(EmployeeEntity.class))).thenReturn(EMPLOYEE);

        var result = employeeRepositoryAdapter.getEmployeeByNumber(EMPLOYEE_NUMBER);

        verify(employeeMapper).mapFromEmployeeNumber(EMPLOYEE_NUMBER);
        verify(employeeJpaRepository).findEmployeeByNumber(NUMBER);
        verify(employeeMapper, never()).toEmployee(any(EmployeeEntity.class));
        assertThat(result).isEmpty();
    }

    @Test
    void getNumberOfStartersOnStartTime() {
        int expectedCount = 1;
        when(employeeJpaRepository.countEmployeesByStartDateEquals(LocalDate.now())).thenReturn(expectedCount);

        var result = employeeRepositoryAdapter.getNumberOfStartersOnStartTime(LocalDate.now());

        verify(employeeJpaRepository).countEmployeesByStartDateEquals(LocalDate.now());
        assertThat(result).isEqualTo(expectedCount);
    }

    @Test
    void commitTrackedChanges() {
        employeeRepositoryAdapter.commitTrackedChanges();

        verify(employeeJpaRepository).flush();
    }

}
