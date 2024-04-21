package pxl.kwops.devops.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.test.RandomExtensions;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TeamTest {

    private String name;
    private Team team;
    private Developer developer;

    @BeforeEach
    void setUp() {
        name = RandomExtensions.nextString();
        team = Team.builder().name(name).build();
        developer = Developer.builder().id("some id").firstName("John").lastName("Doe").build();

        assertThat(team.getId()).isNotNull().isNotEqualTo(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertThat(team.getName()).isEqualTo(name);
        assertThat(team.getDevelopers()).isNotNull().hasSize(0);
    }

    @Test
    void createNew_EmptyName_ShouldThrowContractException() {
        assertThatThrownBy(() -> Team.builder().build())
                .isInstanceOf(ContractException.class);

        assertThatThrownBy(() -> Team.builder().name("").build())
                .isInstanceOf(ContractException.class);
    }

    @Test
    void join_DeveloperNotInTeamYet_ShouldAddDeveloperToTeam() {
        // Arrange
        // Act
        team.join(developer);

        // Assert
        assertThat(team.getDevelopers()).hasSize(1);
        assertThat(team.getDevelopers().getFirst()).isSameAs(developer);
        assertThat(developer.getTeamId()).isEqualTo(team.getId());
    }

    @Test
    void join_DeveloperAlreadyInTeam_ShouldThrowContractException() {
        // Arrange
        team.join(developer);

        // Act + Assert
        assertThatThrownBy(() -> team.join(developer))
                .isInstanceOf(ContractException.class);
    }

    @Test
    void equalsShouldReturnTrueForEqualObjects() {
        // Arrange
        Team team1 = Team.builder().name("Team 1").build();
        Team team2 = mock(Team.class);
        when(team2.getIdComponents()).thenReturn(List.of(team1.getId()));

        // Act + Assert
        assertThat(team1.equals(team2)).isTrue();
    }
}
