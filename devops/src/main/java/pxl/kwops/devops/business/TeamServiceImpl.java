package pxl.kwops.devops.business;

import pxl.kwops.devops.domain.Team;

import java.util.stream.Collectors;

public class TeamServiceImpl implements TeamService {

     private final DeveloperRepositoryAdapter developerRepositoryAdapter;

    public TeamServiceImpl(DeveloperRepositoryAdapter developerRepositoryAdapter) {
        this.developerRepositoryAdapter = developerRepositoryAdapter;
    }

    @Override
    public void assembleDevelopersFor(Team team, int requiredNumberOfDevelopers) {
        var freeDevs = developerRepositoryAdapter.findDevelopersWithoutATeam().stream().limit(requiredNumberOfDevelopers).toList();
        freeDevs.forEach(team::join);

        developerRepositoryAdapter.commitTrackedChanges();
    }
}
