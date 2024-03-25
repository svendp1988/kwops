package pxl.kwops.devops.boundary.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pxl.kwops.devops.boundary.DeveloperMapper;
import pxl.kwops.devops.boundary.DeveloperMapperImpl;
import pxl.kwops.devops.boundary.models.DeveloperEntity;
import pxl.kwops.devops.boundary.models.TeamEntity;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.devops.domain.Percentage;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DataJpaTest
class DeveloperRepositoryAdapterImplIT {


    private TeamEntity team;
    private DeveloperEntity developerWithTeam;
    private DeveloperEntity developerWithoutTeam;
    private final DeveloperMapper developerMapper = new DeveloperMapperImpl();
    private DeveloperRepositoryAdapterImpl developerRepositoryAdapter;

    @SpyBean
    private DeveloperJpaRepository developerJpaRepository;
    @SpyBean
    private TeamJpaRepository teamJpaRepository;

    @BeforeEach
    void setUp() {
        developerRepositoryAdapter = new DeveloperRepositoryAdapterImpl(developerJpaRepository, teamJpaRepository, developerMapper);

        assertThat(developerJpaRepository.count()).isZero();
        assertThat(teamJpaRepository.count()).isZero();

        team = teamJpaRepository.save(TeamEntity.builder()
                .id(UUID.randomUUID())
                .name("Team 1")
                .build());
        developerWithTeam = DeveloperEntity.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .rating(1)
                .teamId(String.valueOf(team.getId()))
                .build();
        developerJpaRepository.save(developerWithTeam);
        developerWithoutTeam = DeveloperEntity.builder()
                .id(2L)
                .firstName("Jane")
                .lastName("Doe")
                .rating(1)
                .build();
        developerJpaRepository.save(developerWithoutTeam);

        developerJpaRepository.flush();
        teamJpaRepository.flush();

        assertThat(developerJpaRepository.count()).isEqualTo(2);
        assertThat(teamJpaRepository.count()).isEqualTo(1);
    }

    @AfterEach
    void tearDown() {
        assertThat(developerJpaRepository.count()).isEqualTo(2);
        assertThat(teamJpaRepository.count()).isEqualTo(1);

        developerJpaRepository.deleteAll();
        teamJpaRepository.deleteAll();

        assertThat(developerJpaRepository.count()).isZero();
        assertThat(teamJpaRepository.count()).isZero();
    }

    @Test
    void findDevelopersWithoutATeam() {
        var result = developerRepositoryAdapter.findDevelopersWithoutATeam();

        assertThat(result).hasSize(1);

        var developer = result.getFirst();
        assertAll(
                () -> assertThat(developer.getFirstName()).isEqualTo("Jane"),
                () -> assertThat(developer.getLastName()).isEqualTo("Doe"),
                () -> assertThat(developer.getRating().getValue()).isEqualTo(1.0),
                () -> assertThat(developer.getTeamId()).isNull()
        );
    }

    @Test
    void updateDevelopersTeamId() {
        var developer = Developer.builder()
                .id(String.valueOf(developerWithoutTeam.getId()))
                .firstName(developerWithoutTeam.getFirstName())
                .lastName(developerWithoutTeam.getLastName())
                .rating(new Percentage(developerWithoutTeam.getRating()))
                .build();

        developerRepositoryAdapter.updateDevelopersTeamId(List.of(developer), String.valueOf(team.getId()));

        assertThat(developerJpaRepository.findByTeamIdIsNull()).hasSize(0);
        assertThat(teamJpaRepository.count()).isEqualTo(1);
    }

}
