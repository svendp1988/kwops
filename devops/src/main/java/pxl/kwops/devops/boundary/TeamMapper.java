package pxl.kwops.devops.boundary;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pxl.kwops.devops.boundary.models.TeamEntity;
import pxl.kwops.devops.domain.Team;

@Mapper(componentModel = "spring")
public interface TeamMapper {

    TeamEntity toTeamEntity(Team team);

    @Mapping(target = "developers", ignore = true)
    Team toTeam(TeamEntity teamEntity);

}
