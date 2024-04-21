package pxl.kwops.devops.business;

import pxl.kwops.devops.domain.Team;

import java.util.List;

public interface TeamService {

    List<Team> getAll();
    Team findById(String id);
    void assembleDevelopersFor(String teamId, int requiredNumberOfDevelopers);

}
