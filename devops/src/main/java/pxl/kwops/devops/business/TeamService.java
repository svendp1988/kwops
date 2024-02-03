package pxl.kwops.devops.business;

import pxl.kwops.devops.domain.Team;

public interface TeamService {

    void assembleDevelopersFor(Team team, int requiredNumberOfDevelopers);

}
