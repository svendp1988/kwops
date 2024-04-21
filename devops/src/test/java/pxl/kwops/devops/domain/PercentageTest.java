package pxl.kwops.devops.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.test.RandomExtensions;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class PercentageTest {

    @Test
    void implicitConvertToDouble_ShouldCorrectlyConvert() {
        double value = RandomExtensions.nextDouble();
        Percentage percentage = new Percentage(value);
        double result = percentage.getValue();
        assertThat(result).isEqualTo(value);
    }

    @Test
    void equalsShouldReturnTrueForEqualObjects() {
        Percentage percentage1 = new Percentage(0.5);
        Percentage percentage2 = new Percentage(0.5);
        assertThat(percentage1).isEqualTo(percentage2);
    }


    @ParameterizedTest
    @MethodSource("invalidInputs")
    void constructor_InputOutOfRange_ShouldThrowContractException(double invalidInput) {
        assertThatThrownBy(() -> new Percentage(invalidInput)).isInstanceOf(ContractException.class);
    }

    @ParameterizedTest
    @MethodSource("toStringCases")
    void toString_ShouldConvertCorrectly(double value, String expected) {
        Percentage percentage = new Percentage(value);
        assertThat(percentage.toString()).isEqualTo(expected);
    }

    private static Stream<Arguments> invalidInputs() {
        return Stream.of(
                Arguments.of(-0.1),
                Arguments.of(1.1)
        );
    }

    private static Stream<Arguments> toStringCases() {
        return Stream.of(
                Arguments.of(0.5, "50%"),
                Arguments.of(1.0, "100%"),
                Arguments.of(0.40123, "40,12%"),
                Arguments.of(0.21987, "21,99%"),
                Arguments.of(0.005, "0,5%")
        );
    }
}
