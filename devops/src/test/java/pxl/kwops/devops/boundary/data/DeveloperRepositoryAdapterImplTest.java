package pxl.kwops.devops.boundary.data;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pxl.kwops.devops.boundary.DeveloperMapper;
import pxl.kwops.devops.boundary.models.DeveloperEntity;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.devops.domain.Percentage;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.openMocks;

class DeveloperRepositoryAdapterImplTest {

    private DeveloperEntity DEVELOPER_ENTITY = DeveloperEntity.builder()
            .id(1L)
            .firstName("John")
            .lastName("Doe")
            .rating(100)
            .teamId("1")
            .build();
    private Developer DEVELOPER = Developer.builder()
            .id(String.valueOf(1L))
            .firstName("John")
            .lastName("Doe")
            .rating(new Percentage(1))
            .teamId(UUID.randomUUID())
            .build();
    private AutoCloseable closeable;
    @Mock
    private DeveloperJpaRepository developerRepository;
    @Mock
    private TeamJpaRepository teamRepository;
    @Mock
    private DeveloperMapper developerMapper;
    @InjectMocks
    private DeveloperRepositoryAdapterImpl developerRepositoryAdapter;

    @BeforeEach
    void setUp() {
        closeable = openMocks(this);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void findDevelopersWithoutATeam() {
        when(developerRepository.findByTeamIdIsNull()).thenReturn(List.of(DEVELOPER_ENTITY));
        when(developerMapper.toDeveloper(DEVELOPER_ENTITY)).thenReturn(DEVELOPER);

        var result = developerRepositoryAdapter.findDevelopersWithoutATeam();

        assertThat(result).containsExactly(DEVELOPER);
        verify(developerRepository).findByTeamIdIsNull();
        verify(developerMapper).toDeveloper(DEVELOPER_ENTITY);
    }

    @Test
    void updateDevelopersTeamId() {
        developerRepositoryAdapter.updateDevelopersTeamId(List.of(DEVELOPER), "1");

        verify(developerRepository).updateTeamId("1", 1L);
        verify(developerRepository).flush();
    }

    @Test
    void commitTrackedChanges() {
        developerRepositoryAdapter.commitTrackedChanges();

        verify(developerRepository).flush();
        verify(teamRepository).flush();
    }
}
