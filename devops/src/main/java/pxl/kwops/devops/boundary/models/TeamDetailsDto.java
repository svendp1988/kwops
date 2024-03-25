package pxl.kwops.devops.boundary.models;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder(builderClassName = "Builder")
@JsonDeserialize(builder = TeamDetailsDto.Builder.class)
public class TeamDetailsDto {

    private String id;
    private String name;
    private List<DeveloperDetailsDto> developers;

}
