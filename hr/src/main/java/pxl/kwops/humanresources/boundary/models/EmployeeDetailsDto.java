package pxl.kwops.humanresources.boundary.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pxl.kwops.humanresources.boundary.models.EmployeeCreateDto;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = EmployeeCreateDto.EmployeeCreateDtoBuilder.class)
public class EmployeeDetailsDto {
    private Long id;
    private String number;
    private String lastName;
    private String firstName;
    private LocalDate startDate;
    private LocalDate endDate;
}
