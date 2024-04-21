package pxl.kwops.devops.business;

import pxl.kwops.devops.domain.Team;

import java.util.List;
import java.util.Optional;

public interface TeamRepositoryAdapter {
    Optional<Team> findById(String id);
    List<Team> getAllTeams();
}
