package pxl.kwops.devops.boundary.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = DeveloperDetailsDto.Builder.class)
public class DeveloperDetailsDto {

    private String id;
    private String firstName;
    private String lastName;
    private double rating;

}
