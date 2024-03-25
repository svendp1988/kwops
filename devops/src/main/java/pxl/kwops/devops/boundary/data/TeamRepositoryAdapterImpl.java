package pxl.kwops.devops.boundary.data;

import lombok.RequiredArgsConstructor;
import pxl.kwops.devops.boundary.TeamMapper;
import pxl.kwops.devops.business.TeamRepositoryAdapter;
import pxl.kwops.devops.domain.Team;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public class TeamRepositoryAdapterImpl implements TeamRepositoryAdapter {

    private final TeamJpaRepository teamRepository;
    private final TeamMapper teamMapper;

    @Override
    public Optional<Team> findById(String id) {
        return teamRepository.findById(UUID.fromString(id))
                .map(teamMapper::toTeam);
    }

    @Override
    public List<Team> getAllTeams() {
        return teamRepository.findAll().stream()
                .map(teamMapper::toTeam)
                .toList();
    }

}
