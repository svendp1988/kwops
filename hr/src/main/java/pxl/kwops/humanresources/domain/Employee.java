package pxl.kwops.humanresources.domain;

import lombok.Builder;
import lombok.Getter;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.domain.models.Entity;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
public class Employee extends Entity {

    private Long id;
    private EmployeeNumber number;
    private String lastName;
    private String firstName;
    private LocalDate startDate;
    private LocalDate endDate;

    public void dismiss(boolean withNotice) {
        if (!withNotice) {
            endDate = LocalDate.now();
        } else {
            if (endDate != null) {
                throw new ContractException("The employee has already been dismissed");
            } else if (Duration.between(startDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() < 90) {
                endDate = LocalDate.now().plusDays(7);
            } else if (Duration.between(startDate.atStartOfDay(), LocalDate.now().atStartOfDay()).toDays() < 365) {
                endDate = LocalDate.now().plusDays(14);
            } else {
                endDate = LocalDate.now().plusDays(28);
            }
        }
    }

    @Override
    protected List<Object> getIdComponents() {
        return List.of(getNumber());
    }

    public static class Factory implements EmployeeFactory {
        @Override
        public Employee createNew(String lastName, String firstName, LocalDate startDate, int sequence) {
            Contracts.require(startDate.isAfter(LocalDate.now().minusYears(1)),
                    "The start date of an employee cannot be more than 1 year in the past");
            Contracts.require(lastName != null && !lastName.isEmpty(), "The last name of an employee cannot be empty");
            Contracts.require(lastName.length() >= 2, "The last name of an employee must have at least 2 characters");
            Contracts.require(firstName != null && !firstName.isEmpty(), "The first name of an employee cannot be empty");
            Contracts.require(firstName.length() >= 2, "The first name of an employee must have at least 2 characters");

            return Employee.builder()
                    .number(new EmployeeNumber(startDate, sequence))
                    .firstName(firstName)
                    .lastName(lastName)
                    .startDate(startDate)
                    .endDate(null)
                    .build();
        }

    }

}
