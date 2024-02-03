package pxl.kwops.devops.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pxl.kwops.devops.boundary.DeveloperMapper;
import pxl.kwops.devops.boundary.TeamMapper;
import pxl.kwops.devops.boundary.data.DeveloperJpaRepository;
import pxl.kwops.devops.boundary.data.DeveloperRepositoryAdapterImpl;
import pxl.kwops.devops.boundary.data.TeamJpaRepository;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;
import pxl.kwops.devops.business.TeamService;
import pxl.kwops.devops.business.TeamServiceImpl;

@Configuration
public class DevopsConfiguration {
    private final DeveloperJpaRepository developerJpaRepository;
    private final TeamJpaRepository teamJpaRepository;
    private final DeveloperMapper developerMapper;
    private final TeamMapper teamMapper;

    @Autowired
    public DevopsConfiguration(DeveloperJpaRepository developerJpaRepository, TeamJpaRepository teamJpaRepository, DeveloperMapper developerMapper, TeamMapper teamMapper) {
        this.developerJpaRepository = developerJpaRepository;
        this.teamJpaRepository = teamJpaRepository;
        this.developerMapper = developerMapper;
        this.teamMapper = teamMapper;
    }

    @Bean(name = "developerMapper")
    public DeveloperMapper developerMapper() {
        return developerMapper;
    }

    @Bean(name = "teamMapper")
    public TeamMapper teamMapper() {
        return teamMapper;
    }

    @Bean
    public DeveloperRepositoryAdapter developerRepositoryAdapter() {
        return new DeveloperRepositoryAdapterImpl(developerJpaRepository, teamJpaRepository, developerMapper);
    }

    @Bean
    public TeamService teamService(DeveloperRepositoryAdapter developerRepositoryAdapter) {
        return new TeamServiceImpl(developerRepositoryAdapter);
    }
}
