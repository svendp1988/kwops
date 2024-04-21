package pxl.kwops.devops.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import pxl.kwops.devops.api.TeamControllerApi;
import pxl.kwops.devops.api.model.TeamAssembleInputDto;
import pxl.kwops.devops.api.model.TeamDetailsDto;
import pxl.kwops.devops.boundary.TeamMapper;
import pxl.kwops.devops.business.TeamService;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.http.ResponseEntity.noContent;
import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
public class TeamController implements TeamControllerApi {

    private final TeamService teamService;
    private final TeamMapper teamMapper;

    @Override
    public ResponseEntity<Void> assembleDevelopersForTeam(TeamAssembleInputDto teamAssembleInputDto) {
        teamService.assembleDevelopersFor(teamAssembleInputDto.getTeamId(), teamAssembleInputDto.getRequiredNumberOfDevelopers());
        return noContent().build();
    }

    @Override
    public ResponseEntity<List<TeamDetailsDto>> getAll() {
        return ok(teamService.getAll().stream()
                .map(teamMapper::toTeamDetailsDto)
                .collect(toList()));
    }

}
