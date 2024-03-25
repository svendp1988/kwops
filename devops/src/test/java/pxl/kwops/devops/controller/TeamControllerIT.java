package pxl.kwops.devops.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pxl.kwops.api.models.ErrorModel;
import pxl.kwops.devops.boundary.data.TeamJpaRepository;
import pxl.kwops.devops.boundary.models.DeveloperEntity;
import pxl.kwops.devops.boundary.models.TeamAssembleInputDto;
import pxl.kwops.devops.boundary.models.TeamEntity;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class TeamControllerIT {

    private static final String CONTEXT_PATH = "/api";

    @MockBean
    private TeamJpaRepository teamRepositoryMock;

    @MockBean
    private DeveloperRepositoryAdapter developerRepositoryAdapterMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void getAll_noneFound() throws Exception {
        mockMvc.perform(get("/api/teams").contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));

        verify(teamRepositoryMock).findAll();
    }

    @Test
    void getAll_found() throws Exception {
        UUID team1Id = UUID.fromString("07745914-0db6-3595-ae18-eda8c6bf6fc5");
        UUID team2Id = UUID.fromString("92bb4614-1a46-3742-b77f-d726831080dc");
        var team1 = TeamEntity.builder()
                .id(team1Id)
                .name("Team 1")
                .developers(
                        List.of(DeveloperEntity.builder()
                                        .id(1L)
                                        .teamId(team1Id.toString())
                                        .firstName("John")
                                        .lastName("Doe")
                                        .rating(1)
                                        .build(),
                                DeveloperEntity.builder()
                                        .id(2L)
                                        .teamId(team1Id.toString())
                                        .firstName("Jane")
                                        .lastName("Doe")
                                        .rating(0.5)
                                        .build())
                )
                .build();
        var team2 = TeamEntity.builder()
                .id(team2Id)
                .name("Team 2")
                .build();
        when(teamRepositoryMock.findAll()).thenReturn(List.of(
                team1,
                team2
        ));

        mockMvc.perform(get("/api/teams").contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().string("[" +
                        "{" +
                        "\"id\":\"07745914-0db6-3595-ae18-eda8c6bf6fc5\"," +
                        "\"name\":\"Team 1\"," +
                        "\"developers\":[" +
                        "{" +
                        "\"id\":\"1\"," +
                        "\"firstName\":\"John\"," +
                        "\"lastName\":\"Doe\"," +
                        "\"rating\":1.0" +
                        "}," +
                        "{" +
                        "\"id\":\"2\"," +
                        "\"firstName\":\"Jane\"," +
                        "\"lastName\":\"Doe\"," +
                        "\"rating\":0.5" +
                        "}]" +
                        "}," +
                        "{" +
                        "\"id\":\"92bb4614-1a46-3742-b77f-d726831080dc\"," +
                        "\"name\":\"Team 2\"," +
                        "\"developers\":[]" +
                        "}]"));

        verify(teamRepositoryMock).findAll();
    }

    @Test
    void assembleTeam_noTeamFound() throws Exception {
        var teamId = "07745914-0db6-3595-ae18-eda8c6bf6fc5";
        var teamAssembleInputDto = TeamAssembleInputDto.builder()
                .withTeamId(teamId)
                .withRequiredNumberOfDevelopers(3)
                .build();
        var errorMessage = "Could not find team with id: " + teamId;
        var expectedError = new ErrorModel(errorMessage, 400);
        when(teamRepositoryMock.findById(UUID.fromString(teamId))).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/teams/assemble").contextPath(CONTEXT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamAssembleInputDto))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedError)));

        verify(teamRepositoryMock).findById(UUID.fromString(teamId));
    }

    @Test
    void assembleTeam_teamFound() throws Exception {
        var teamId = "07745914-0db6-3595-ae18-eda8c6bf6fc5";
        var teamAssembleInputDto = TeamAssembleInputDto.builder()
                .withTeamId(teamId)
                .withRequiredNumberOfDevelopers(3)
                .build();

        when(teamRepositoryMock.findById(UUID.fromString(teamId))).thenReturn(Optional.of(
                TeamEntity.builder()
                        .id(UUID.fromString(teamId))
                        .name("Team 1")
                        .build()
        ));

        mockMvc.perform(post("/api/teams/assemble").contextPath(CONTEXT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(teamAssembleInputDto))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNoContent());

        verify(teamRepositoryMock).findById(UUID.fromString(teamId));
    }

}
