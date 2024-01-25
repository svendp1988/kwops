package pxl.kwops.humanresources.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.test.RandomExtensions;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeFactoryTest {

    private Employee.Factory factory;
    private String lastName;
    private String firstName;
    private LocalDate startDate;
    private int sequence;

    @BeforeEach
    public void beforeEachTest() {
        factory = new Employee.Factory();

        lastName = RandomExtensions.nextString();
        firstName = RandomExtensions.nextString();
        startDate = RandomExtensions.nextDateTimeInFuture();
        sequence = RandomExtensions.nextPositive(1000);
    }

    @Test
    public void createNewValidInputShouldInitializeFieldsCorrectly() {
        // Act
        Employee employee = factory.createNew(lastName, firstName, startDate, sequence);
        assertAll(() -> {
            // Assert
            assertThat(new EmployeeNumber(startDate, sequence)).isEqualTo(employee.getNumber());
            assertThat(firstName).isEqualTo(employee.getFirstName());
            assertThat(lastName).isEqualTo(employee.getLastName());
            assertThat(startDate).isEqualTo(employee.getStartDate());
            assertThat(employee.getEndDate()).isNull();
        });
    }

    @Test
    public void createNewStartDateMoreThanAYearAgoShouldThrowContractException() {
        LocalDate invalidStartDate = LocalDate.now().minusYears(1);
        assertThrows(ContractException.class, () ->
                factory.createNew(lastName, firstName, invalidStartDate, sequence));
    }

    @Test
    public void createNewInvalidFirstNameShouldThrowContractException() {
        assertAll(() -> {
            assertThrows(ContractException.class, () ->
                    factory.createNew(lastName, null, startDate, sequence));
            assertThrows(ContractException.class, () ->
                    factory.createNew(lastName, "a", startDate, sequence));
        });
    }

    @Test
    public void createNewInvalidLastNameShouldThrowContractException() {
        assertAll(() -> {
            assertThrows(ContractException.class, () ->
                    factory.createNew(null, firstName, startDate, sequence));
            assertThrows(ContractException.class, () ->
                    factory.createNew("a", firstName, startDate, sequence));
        });
    }
}
