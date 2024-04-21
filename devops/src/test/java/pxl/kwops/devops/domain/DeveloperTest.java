package pxl.kwops.devops.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import pxl.kwops.test.RandomExtensions;
import pxl.kwops.domain.exceptions.ContractException;

import org.junit.jupiter.api.function.Executable;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class DeveloperTest {
    private static final String id = RandomExtensions.nextString();
    private static final String firstName = RandomExtensions.nextString();
    private static final String lastName = RandomExtensions.nextString();
    private static final Percentage rating = new Percentage(RandomExtensions.nextDouble());


    @Test
    void createNew_ValidInput_ShouldInitializeFieldsCorrectly() {
        // Act
        var developer = Developer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .rating(rating)
                .build();

        // Assert
        assertThat(developer.getId()).isEqualTo(id);
        assertThat(developer.getFirstName()).isEqualTo(firstName);
        assertThat(developer.getLastName()).isEqualTo(lastName);
        assertThat(developer.getRating()).isEqualTo(rating);
    }

    @Test
    void createNew_ValidInput_ShouldSetTeamIdToNull() {
        // Act
        var developer = Developer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .rating(rating)
                .build();

        // Assert
        assertThat(developer.getTeamId()).isNull();
    }

    @Test
    void equalsShouldReturnTrueForEqualObjects() {
        // Arrange
        var developer1 = Developer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .rating(rating)
                .build();
        var developer2 = Developer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .rating(rating)
                .build();

        // Act
        // Assert
        assertThat(developer1.equals(developer2)).isTrue();
    }

    @Test
    void setTeamId() {
        // Arrange
        var developer = Developer.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .rating(rating)
                .build();
        var teamId = RandomExtensions.nextUUID();

        // Act
        assertThat(developer.getTeamId()).isNull();
        developer.setTeamId(teamId);

        // Assert
        assertThat(developer.getTeamId()).isEqualTo(teamId);
    }

    @ParameterizedTest
    @MethodSource("testCases")
    void createNew_InvalidInput_ShouldThrowContractException(Executable executable, String errorMessage) {
        // Assert
        assertThrowsContractException(executable, errorMessage);
    }

    private static Stream<Arguments> testCases() {
        return Stream.of(
                arguments((Executable) () -> Developer.builder()
                        .firstName(firstName)
                        .lastName(lastName)
                        .rating(rating)
                        .build(), "identifier"),
                arguments((Executable) () -> Developer.builder()
                        .id("")
                        .firstName(firstName)
                        .lastName(lastName)
                        .rating(rating)
                        .build(), "identifier"),
                arguments((Executable) () -> Developer.builder()
                        .id(id)
                        .lastName(lastName)
                        .rating(rating)
                        .build(), "first name"),
                arguments((Executable) () -> Developer.builder()
                        .id(id)
                        .firstName("")
                        .lastName(lastName)
                        .rating(rating)
                        .build(), "first name"),
                arguments((Executable) () -> Developer.builder()
                        .id(id)
                        .firstName(firstName)
                        .rating(rating)
                        .build(), "last name"),
                arguments((Executable) () -> Developer.builder()
                        .id(id)
                        .firstName(firstName)
                        .lastName("")
                        .rating(rating)
                        .build(), "last name")
        );
    }

    private void assertThrowsContractException(Executable executable, String field) {
        assertThat(assertThrows(ContractException.class, executable).getMessage()).isEqualTo(String.format("The %s of a developer cannot be empty", field));
    }
}
