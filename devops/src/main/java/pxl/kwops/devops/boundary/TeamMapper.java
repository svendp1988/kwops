package pxl.kwops.devops.boundary;


import org.mapstruct.Mapper;
import pxl.kwops.devops.api.model.TeamDetailsDto;
import pxl.kwops.devops.boundary.models.TeamEntity;
import pxl.kwops.devops.domain.Team;

@Mapper(componentModel = "spring", uses = DeveloperMapper.class)
public interface TeamMapper {

    TeamEntity toTeamEntity(Team team);

    Team toTeam(TeamEntity teamEntity);

    TeamDetailsDto toTeamDetailsDto(Team team);

}
