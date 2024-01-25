package pxl.kwops.humanresources.domain;

import org.junit.jupiter.api.Test;
import pxl.kwops.domain.exceptions.ContractException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class EmployeeTest {

    @Test
    void dismissWithoutNoticeShouldSetEndDateOnToday() {

        // Arrange
        Employee employee = Employee.builder()
                .build();

        // Act
        employee.dismiss(false);

        // Assert
        LocalDate today = LocalDate.now();
        LocalDate endDate = employee.getEndDate();
        assertThat(today).isEqualTo(endDate);
    }

    @Test
    void dismissWithoutNoticeEmployeeAlreadyHasEndDateShouldSetEndDateOnToday() {
        // Arrange
        LocalDate endDateInFuture = LocalDate.now().plusDays(5);
        Employee employee = Employee.builder()
                .endDate(endDateInFuture)
                .build();

        // Act
        employee.dismiss(false);

        // Assert
        LocalDate today = LocalDate.now();
        LocalDate endDate = employee.getEndDate();
        assertThat(today).isEqualTo(endDate);
    }

    @Test
    void dismissWithNoticeEmployeeAlreadyHasEndDateShouldThrowContractException() {
        // Arrange
        LocalDate endDateInFuture = LocalDate.now().plusDays(5);
        Employee employee = Employee.builder()
                .endDate(endDateInFuture)
                .build();

        // Act + Assert
        assertThrows(ContractException.class, () -> employee.dismiss(true));
    }

    @Test
    void dismissWithNoticeLessThan3MonthsInServiceShouldSetEndDateInOneWeek() {
        // Arrange
        LocalDate lessThan3MonthsAgo = LocalDate.now().minusDays(28);
        Employee employee = Employee.builder()
                .startDate(lessThan3MonthsAgo)
                .endDate(null)
                .build();

        // Act
        employee.dismiss(true);

        // Assert
        LocalDate over1Week = LocalDate.now().plusDays(7);
        LocalDate endDate = employee.getEndDate();
        assertThat(over1Week).isEqualTo(endDate);
    }

    @Test
    void dismissWithNoticeLessThan12MonthsInServiceShouldSetEndDateIn2Weeks() {
        // Arrange
        LocalDate lessThan12MonthsAgo = LocalDate.now().minusMonths(10);
        Employee employee = Employee.builder()
                .startDate(lessThan12MonthsAgo)
                .endDate(null)
                .build();

        // Act
        employee.dismiss(true);

        // Assert
        LocalDate over2Weeks = LocalDate.now().plusDays(14);
        LocalDate endDate = employee.getEndDate();
        assertThat(over2Weeks).isEqualTo(endDate);
    }

    @Test
    void dismissWithNoticeMoreThan12MonthsInServiceShouldSetEndDateIn4Weeks() {
        // Arrange
        LocalDate moreThan12MonthsAgo = LocalDate.now().minusYears(1);
        Employee employee = Employee.builder()
                .startDate(moreThan12MonthsAgo)
                .endDate(null)
                .build();

        // Act
        employee.dismiss(true);

        // Assert
        LocalDate over4Weeks = LocalDate.now().plusDays(28);
        LocalDate endDate = employee.getEndDate();
        assertThat(over4Weeks).isEqualTo(endDate);
    }

    @Test
    void equalsShouldReturnTrueForEqualObjects() {
        // Arrange
        EmployeeNumber number = new EmployeeNumber(LocalDate.now(), 1);
        Employee employee1 = Employee.builder()
                .number(number)
                .build();
        Employee employee2 = Employee.builder()
                .number(number)
                .build();

        // Act + Assert
        assertThat(employee1.equals(employee2)).isTrue();
    }
}
