package pxl.kwops.humanresources.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import pxl.kwops.humanresources.api.model.EmployeeCreateDto;
import pxl.kwops.humanresources.api.model.EmployeeDetailsDto;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.business.EmployeeService;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeControllerTest {

    @Mock
    private EmployeeService employeeServiceMock;

    @Mock
    private EmployeeMapper mapperMock;

    @InjectMocks
    private EmployeeController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addShouldUseServiceAndReturnLocation() {
        // Arrange
        var inputModel = EmployeeCreateDto.builder()
                .firstName("John")
                .lastName("Doe")
                .startDate(LocalDate.now())
                .build();

        var hiredEmployee = mock(Employee.class);
        when(employeeServiceMock.hireNewEmployee(anyString(), anyString(), any(LocalDate.class))).thenReturn(hiredEmployee);

        // Mock RequestContextHolder
        ServletRequestAttributes attributesMock = Mockito.mock(ServletRequestAttributes.class);
        when(attributesMock.getRequest()).thenReturn(Mockito.mock(HttpServletRequest.class));
        RequestContextHolder.setRequestAttributes(attributesMock);

        // Act
        var result = controller.add(inputModel);

        // Assert
        verify(employeeServiceMock, times(1)).hireNewEmployee(eq(inputModel.getLastName()), eq(inputModel.getFirstName()), any(LocalDate.class));

        assertAll(() -> assertThat(result).isNotNull(),
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED),
                () -> assertThat(result.getHeaders().getLocation().getPath()).isEqualTo("/0"));

        RequestContextHolder.resetRequestAttributes();
    }

    @Test
    public void getByNumberShouldUseServiceAndMapResult() {
        // Arrange
        var employeeNumber = new EmployeeNumber(LocalDate.now(), 1);
        when(mapperMock.mapToEmployeeNumber(anyString())).thenReturn(employeeNumber);

        var employee = mock(Employee.class);
        when(employeeServiceMock.getEmployeeByNumber(any(EmployeeNumber.class))).thenReturn(Optional.ofNullable(employee));

        var outputModel = EmployeeDetailsDto.builder().build();
        when(mapperMock.toEmployeeDetailsDto(any(Employee.class))).thenReturn(outputModel);

        // Act
        var result = controller.getByNumber(String.valueOf(employeeNumber));

        // Assert
        verify(mapperMock, times(1)).mapToEmployeeNumber(eq(String.valueOf(employeeNumber)));
        verify(employeeServiceMock, times(1)).getEmployeeByNumber(eq(employeeNumber));
        verify(mapperMock, times(1)).toEmployeeDetailsDto(eq(employee));

        assertAll(() -> assertThat(result).isNotNull(),
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(result.getBody()).isEqualTo(outputModel));
    }

    @Test
    public void getByNumberEmployeeDoesNotExistShouldReturnNotFound() {
        // Arrange
        var employeeNumber = new EmployeeNumber(LocalDate.now(), 1);
        when(mapperMock.mapToEmployeeNumber(anyString())).thenReturn(employeeNumber);

        when(employeeServiceMock.getEmployeeByNumber(any(EmployeeNumber.class))).thenReturn(Optional.empty());

        // Act
        var result = controller.getByNumber(String.valueOf(employeeNumber));

        // Assert
        verify(mapperMock, times(1)).mapToEmployeeNumber(eq(String.valueOf(employeeNumber)));
        verify(employeeServiceMock, times(1)).getEmployeeByNumber(eq(employeeNumber));

        assertAll(() -> assertThat(result).isNotNull(),
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND),
                () -> assertThat(result.getBody()).isNull());
    }

    @Test
    void dismissShouldUseServiceAndReturnOk() {
        // Arrange
        var employeeNumber = new EmployeeNumber(LocalDate.now(), 1);
        when(mapperMock.mapToEmployeeNumber(anyString())).thenReturn(employeeNumber);
        var withNotice = true;

        // Act
        var result = controller.dismiss(String.valueOf(employeeNumber), withNotice);

        // Assert
        verify(mapperMock, times(1)).mapToEmployeeNumber(eq(String.valueOf(employeeNumber)));
        verify(employeeServiceMock, times(1)).dismissEmployee(eq(employeeNumber), eq(withNotice));

        assertAll(() -> assertThat(result).isNotNull(),
                () -> assertThat(result.getStatusCode()).isEqualTo(HttpStatus.OK),
                () -> assertThat(result.getBody()).isNull());
    }

}
