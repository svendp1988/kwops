package pxl.kwops.devops.business;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.devops.domain.Team;
import pxl.kwops.test.RandomExtensions;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class TeamServiceImplTest {

    private Team team;

    @Mock
    private DeveloperRepositoryAdapter developerRepositoryAdapterMock;

    @InjectMocks
    private TeamServiceImpl service;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

        team = Team.builder().name(RandomExtensions.nextString()).build();
    }

    @Test
    public void assembleDevelopersAsyncFor_EnoughDevelopersAvailable_ShouldRandomlyAddRequiredNumberOfDevelopers() {
        // Arrange
        int requiredNumberOfDevelopers = 2;
        List<Developer> freeDevelopers = new ArrayList<>();
        for (int i = 0; i < requiredNumberOfDevelopers + 2; i++) {
            freeDevelopers.add(Developer.builder()
                    .id(RandomExtensions.nextString())
                    .firstName(RandomExtensions.nextString())
                    .lastName(RandomExtensions.nextString())
                    .build());
        }

        when(developerRepositoryAdapterMock.findDevelopersWithoutATeam()).thenReturn(freeDevelopers);

        // Act
        service.assembleDevelopersFor(team, requiredNumberOfDevelopers);

        // Assert
        verify(developerRepositoryAdapterMock, times(1)).findDevelopersWithoutATeam();
        assertThat(team.getDevelopers()).hasSize(requiredNumberOfDevelopers)
                .allMatch(freeDevelopers::contains);
        verify(developerRepositoryAdapterMock, times(1)).commitTrackedChanges();
    }

    @Test
    public void assembleDevelopersAsyncFor_NotEnoughDevelopersAvailable_ShouldAddAllAvailableDevelopers() {
        // Arrange
        int requiredNumberOfDevelopers = 5;
        int numberOfAvailableDevelopers = requiredNumberOfDevelopers - 2;
        List<Developer> freeDevelopers = new ArrayList<>();
        for (int i = 0; i < numberOfAvailableDevelopers; i++) {
            freeDevelopers.add(Developer.builder()
                    .id(RandomExtensions.nextString())
                    .firstName(RandomExtensions.nextString())
                    .lastName(RandomExtensions.nextString())
                    .build());
        }

        when(developerRepositoryAdapterMock.findDevelopersWithoutATeam()).thenReturn(freeDevelopers);

        // Act
        service.assembleDevelopersFor(team, requiredNumberOfDevelopers);

        // Assert
        verify(developerRepositoryAdapterMock, times(1)).findDevelopersWithoutATeam();
        assertThat(team.getDevelopers()).hasSize(numberOfAvailableDevelopers)
                .allMatch(freeDevelopers::contains);
        verify(developerRepositoryAdapterMock, times(1)).commitTrackedChanges();
    }
}
