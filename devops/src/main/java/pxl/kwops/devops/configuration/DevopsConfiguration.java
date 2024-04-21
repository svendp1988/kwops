package pxl.kwops.devops.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import pxl.kwops.devops.boundary.DeveloperMapper;
import pxl.kwops.devops.boundary.DeveloperMapperImpl;
import pxl.kwops.devops.boundary.TeamMapper;
import pxl.kwops.devops.boundary.TeamMapperImpl;
import pxl.kwops.devops.boundary.data.DeveloperJpaRepository;
import pxl.kwops.devops.boundary.data.DeveloperRepositoryAdapterImpl;
import pxl.kwops.devops.boundary.data.TeamJpaRepository;
import pxl.kwops.devops.boundary.data.TeamRepositoryAdapterImpl;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;
import pxl.kwops.devops.business.TeamRepositoryAdapter;
import pxl.kwops.devops.business.TeamService;
import pxl.kwops.devops.business.TeamServiceImpl;

@Configuration
public class DevopsConfiguration {

    @Bean(name = "developerMapper")
    public DeveloperMapper developerMapper() {
        return new DeveloperMapperImpl();
    }

    @Bean(name = "teamMapper")
    @DependsOn("developerMapper")
    public TeamMapper teamMapper() {
        return new TeamMapperImpl();
    }

    @Bean
    public DeveloperRepositoryAdapter developerRepositoryAdapter(DeveloperJpaRepository developerJpaRepository, TeamJpaRepository teamJpaRepository, DeveloperMapper developerMapper) {
        return new DeveloperRepositoryAdapterImpl(developerJpaRepository, teamJpaRepository, developerMapper);
    }

    @Bean
    public TeamRepositoryAdapter teamRepositoryAdapter(TeamJpaRepository teamJpaRepository, TeamMapper teamMapper) {
        return new TeamRepositoryAdapterImpl(teamJpaRepository, teamMapper);
    }

    @Bean
    public TeamService teamService(DeveloperRepositoryAdapter developerRepositoryAdapter, TeamRepositoryAdapter teamRepositoryAdapter) {
        return new TeamServiceImpl(developerRepositoryAdapter, teamRepositoryAdapter);
    }

}
