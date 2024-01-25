package pxl.kwops.humanresources.boundary.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder(setterPrefix = "with")
@Getter
@JsonDeserialize(builder = EmployeeCreateDto.EmployeeCreateDtoBuilder.class)
public class EmployeeCreateDto {
    private final String lastName;
    private final String firstName;
    private final LocalDate startDate;
}
