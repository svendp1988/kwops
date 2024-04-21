package pxl.kwops.devops.boundary.data;

import lombok.RequiredArgsConstructor;
import pxl.kwops.devops.boundary.DeveloperMapper;
import pxl.kwops.devops.boundary.models.DeveloperEntity;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;
import pxl.kwops.devops.domain.Developer;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class DeveloperRepositoryAdapterImpl implements DeveloperRepositoryAdapter {

    private final DeveloperJpaRepository developerRepository;
    private final TeamJpaRepository teamRepository;
    private final DeveloperMapper developerMapper;

    @Override
    public Developer addDeveloper(Developer developer) {
        DeveloperEntity entity = developerRepository.save(developerMapper.toDeveloperEntity(developer));

        return developerMapper.toDeveloper(entity);
    }

    @Override
    public Optional<Developer> findById(String id) {
        return developerRepository.findById(Long.parseLong(id))
                .map(developerMapper::toDeveloper);
    }

    @Override
    public List<Developer> findDevelopersWithoutATeam() {
        return developerRepository.findByTeamIdIsNull().stream()
                .map(developerMapper::toDeveloper)
                .toList();
    }

    @Override
    public void updateDevelopersTeamId(List<Developer> developers, String teamId) {
        developers.forEach(developer -> developerRepository.updateTeamId(teamId, Long.parseLong(developer.getId())));
        commitTrackedChanges();
    }

    @Override
    public void commitTrackedChanges() {
        developerRepository.flush();
        teamRepository.flush();
    }
}
