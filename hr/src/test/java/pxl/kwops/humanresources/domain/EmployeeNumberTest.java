package pxl.kwops.humanresources.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.test.RandomExtensions;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeNumberTest {

    @Test
    public void constructorFromStartDateAndSequenceShouldInitializeProperly() {
        // Arrange
        var startDate = RandomExtensions.nextDateTimeInFuture();
        var sequence = RandomExtensions.nextPositive();

        // Act
        var number = new EmployeeNumber(startDate, sequence);

        // Assert
        assertAll(() -> assertThat(startDate.getYear()).isEqualTo(number.getYear()),
                () -> assertThat(startDate.getMonthValue()).isEqualTo(number.getMonth()),
                () -> assertThat(startDate.getDayOfMonth()).isEqualTo(number.getDay()),
                () -> assertThat(sequence).isEqualTo(number.getSequence()));
    }

    @Test
    public void constructorFromStartDateAndSequenceInvalidSequenceShouldThrowContractException() {
        // Arrange
        var startDate = RandomExtensions.nextDateTimeInFuture();
        var invalidSequence = RandomExtensions.nextZeroOrNegative();

        // Act + Assert
        assertThrows(ContractException.class, () -> new EmployeeNumber(startDate, invalidSequence));
    }

    @Test
    public void constructorFromStringShouldInitializeProperly() {
        // Act
        var number = new EmployeeNumber("19990101001");

        // Assert
        assertAll(() -> assertThat(1999).isEqualTo(number.getYear()),
                () -> assertThat(1).isEqualTo(number.getMonth()),
                () -> assertThat(1).isEqualTo(number.getDay()),
                () -> assertThat(1).isEqualTo(number.getSequence()));
    }

    @Test
    public void constructorFromStringInvalidInputShouldThrowContractException() {
        assertAll(() -> assertThrows(ContractException.class, () -> new EmployeeNumber("")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("1")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("abcdefghikj")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("00001231001")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("19991331001")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("19990031001")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("19991200001")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("19991232001")),
                () -> assertThrows(ContractException.class, () -> new EmployeeNumber("19991231000")));
    }

    @ParameterizedTest
    @MethodSource("toStringCases")
    public void toStringShouldCorrectlyConvert(LocalDate startDate, int sequence, String expected) {
        // Act
        EmployeeNumber number = new EmployeeNumber(startDate, sequence);
        String result = number.toString();

        // Assert
        assertThat(expected).isEqualTo(result);
    }

    @Test
    public void equalsShouldReturnTrueForEqualObjects() {
        // Arrange
        LocalDate startDate1 = LocalDate.of(2000, 12, 29);
        LocalDate startDate2 = LocalDate.of(2000, 12, 29);
        int sequence = 987;
        EmployeeNumber number1 = new EmployeeNumber(startDate1, sequence);
        EmployeeNumber number2 = new EmployeeNumber(startDate2, sequence);

        // Act

        // Assert
        assertThat(number1.equals(number2)).isTrue();
    }

    private static Stream<Arguments> toStringCases() {
        return Stream.of(
                Arguments.of(LocalDate.of(2000, 12, 29), 987, "20001229987"),
                Arguments.of(LocalDate.of(1, 2, 3), 4, "00010203004") // with padded zeros
        );
    }

}
