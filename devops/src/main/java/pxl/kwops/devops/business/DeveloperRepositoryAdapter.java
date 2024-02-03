package pxl.kwops.devops.business;

import pxl.kwops.devops.domain.Developer;

import java.util.List;
import java.util.Optional;

public interface DeveloperRepositoryAdapter {
    Developer addDeveloper(Developer developer);
    Optional<Developer> findById(String id);
    List<Developer> findDevelopersWithoutATeam();
    void updateDevelopersTeamId(List<Developer> developers, String teamId);
    void commitTrackedChanges();
}
