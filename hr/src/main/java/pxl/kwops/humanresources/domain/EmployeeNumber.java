package pxl.kwops.humanresources.domain;

import lombok.Builder;
import lombok.Getter;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.domain.models.ValueObject;

import java.time.LocalDate;
import java.util.List;

@Getter
public class EmployeeNumber extends ValueObject {
    private final int year;
    private final int month;
    private final int day;
    private final int sequence;

    public EmployeeNumber(LocalDate startDate, int sequence)
    {
        Contracts.require(sequence >= 1, "The sequence in the employee number must be a positive number");

        this.year = startDate.getYear();
        this.month = startDate.getMonthValue();
        this.day = startDate.getDayOfMonth();
        this.sequence = sequence;
    }

    public EmployeeNumber(String value)
    {
        Contracts.require(!value.isEmpty(), "An employee number cannot be empty");
        Contracts.require(value.length() == 11, "An employee number must have exactly 11 characters");
        Contracts.require(value.chars().allMatch(Character::isDigit), "An employee number can only contain digits");

        year = Integer.parseInt(value.substring(0, 4));
        Contracts.require(year > 0, "The first 4 digits of an employee number must be a valid year");

        month = Integer.parseInt(value.substring(4, 6));
        Contracts.require(month >= 1 && month <= 12, "Digits 5 and 6 of an employee number must be a valid month");

        day = Integer.parseInt(value.substring(6, 8));
        Contracts.require(day >= 1 && day <= 31, "Digits 7 and 8 of an employee number must be a valid day");

        sequence = Integer.parseInt(value.substring(8, 11));
        Contracts.require(sequence >= 1, "The sequence in the employee number must be a positive number");
    }

    @Override
    protected List<Object> getEqualityComponents() {
        return List.of(getYear(), getMonth(), getDay(), getSequence());
    }

    @Override
    public String toString()
    {
        return String.format("%04d%02d%02d%03d", getYear(), getMonth(), getDay(), getSequence());
    }

}
