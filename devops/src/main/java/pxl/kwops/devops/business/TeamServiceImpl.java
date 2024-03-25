package pxl.kwops.devops.business;

import lombok.RequiredArgsConstructor;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.devops.domain.Team;
import pxl.kwops.domain.exceptions.ContractException;

import java.util.List;

@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

     private final DeveloperRepositoryAdapter developerRepositoryAdapter;
     private final TeamRepositoryAdapter teamRepositoryAdapter;

    @Override
    public List<Team> getAll() {
        return teamRepositoryAdapter.getAllTeams();
    }

    @Override
    public Team findById(String id) {
        return teamRepositoryAdapter.findById(id).orElseThrow(() -> new ContractException("Could not find team with id: " + id));
    }

    @Override
    public void assembleDevelopersFor(String teamId, int requiredNumberOfDevelopers) {
        var team = findById(teamId);
        findFreeDevs(requiredNumberOfDevelopers).forEach(team::join);

        developerRepositoryAdapter.commitTrackedChanges();
    }

    private List<Developer> findFreeDevs(int requiredNumberOfDevelopers) {
        return developerRepositoryAdapter.findDevelopersWithoutATeam().stream()
                .limit(requiredNumberOfDevelopers)
                .toList();
    }
}
