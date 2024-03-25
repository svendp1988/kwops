package pxl.kwops.devops.boundary.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;

@Builder(setterPrefix = "with")
@Getter
@JsonDeserialize(builder = TeamAssembleInputDto.TeamAssembleInputDtoBuilder.class)
public class TeamAssembleInputDto {

    private final String teamId;
    private final int requiredNumberOfDevelopers;

}
