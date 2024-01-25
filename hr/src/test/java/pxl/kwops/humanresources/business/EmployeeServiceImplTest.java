package pxl.kwops.humanresources.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;
import pxl.kwops.test.RandomExtensions;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Optional;

public class EmployeeServiceImplTest {

    private EmployeeRepositoryAdapter employeeRepositoryAdapterMock;
    private EmployeeService service;

    @BeforeEach
    public void setUp() {
        employeeRepositoryAdapterMock = mock(EmployeeRepositoryAdapter.class);
        service = new EmployeeServiceImpl(employeeRepositoryAdapterMock);
    }

    @Test
    public void hireNewAsyncShouldRetrieveNumberOfStartersCreateTheEmployeeAndSaveIt() {
        // Arrange
        var lastName = RandomExtensions.nextString();
        var firstName = RandomExtensions.nextString();
        var startDate = LocalDate.now();
        var numberOfStartersOnStartDate = RandomExtensions.nextPositive(1000);
        var expectedSequence = numberOfStartersOnStartDate + 1;

        when(employeeRepositoryAdapterMock.getNumberOfStartersOnStartTime(any(LocalDate.class)))
                .thenReturn(numberOfStartersOnStartDate);
        when(employeeRepositoryAdapterMock.addEmployee(any(Employee.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        var result = service.hireNewEmployee(lastName, firstName, startDate);

        // Assert
        verify(employeeRepositoryAdapterMock, times(1)).getNumberOfStartersOnStartTime(startDate);
        verify(employeeRepositoryAdapterMock, times(1)).addEmployee(any(Employee.class));
        assertAll(
                () -> assertThat(result.getNumber().getSequence()).isEqualTo(expectedSequence),
                () -> assertThat(result.getFirstName()).isEqualTo(firstName),
                () -> assertThat(result.getLastName()).isEqualTo(lastName),
                () -> assertThat(result.getStartDate()).isEqualTo(startDate)
        );
    }

    @Test
    public void dismissAsyncShouldRetrieveEmployeeFromRepositoryDismissTheEmployeeAndSaveTheChanges() throws Exception {
        // Arrange
        var employeeNumber = new EmployeeNumber(LocalDate.now(), 1);
        var employeeToDismiss = mock(Employee.class);

        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenReturn(Optional.ofNullable(employeeToDismiss));

        // Act
        service.dismissEmployee(employeeNumber, true);

        // Assert
        verify(employeeRepositoryAdapterMock, times(1)).getEmployeeByNumber(employeeNumber);
        verify(employeeToDismiss, times(1)).dismiss(true);
        verify(employeeRepositoryAdapterMock, times(1)).commitTrackedChanges();
    }

}
