package pxl.kwops.humanresources.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import pxl.kwops.api.models.ErrorModel;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.humanresources.api.model.EmployeeCreateDto;
import pxl.kwops.humanresources.business.EmployeeRepositoryAdapter;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageSender;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class EmployeeControllerIT {

    private static final String CONTEXT_PATH = "/api";

    @MockBean
    private EmployeeRepositoryAdapter employeeRepositoryAdapterMock;

    @MockBean
    private MessageSender<EmployeeHiredMessage> messageSenderMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(authorities = "read:kwops")
    void getByNumber_notFound() throws Exception {
        var argumentCaptor = ArgumentCaptor.forClass(EmployeeNumber.class);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class))).thenReturn(Optional.empty());

        var employeeNumber = "20240101001";
        mockMvc.perform(get("/api/employees/" + employeeNumber).contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isNotFound());

        verify(employeeRepositoryAdapterMock).getEmployeeByNumber(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue()).isEqualTo(new EmployeeNumber(employeeNumber));
    }

    @Test
    @WithMockUser(authorities = "read:kwops")
    void getByNumber_found() throws Exception {
        var employeeNumber = "20240101001";
        var number = new EmployeeNumber(employeeNumber);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenReturn(Optional.of(Employee.builder()
                        .id(1L)
                        .number(number)
                        .firstName("John")
                        .lastName("Doe")
                        .startDate(LocalDate.of(2024, 1, 1))
                        .endDate(null)
                        .build()));

        mockMvc.perform(get("/api/employees/" + employeeNumber).contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk())
                .andExpect(content().string("{" +
                        "\"id\":1," +
                        "\"number\":\"20240101001\"," +
                        "\"lastName\":\"Doe\"," +
                        "\"firstName\":\"John\"," +
                        "\"startDate\":\"2024-01-01\"," +
                        "\"endDate\":null}"));

        verify(employeeRepositoryAdapterMock).getEmployeeByNumber(number);
    }

    @Test
    @WithAnonymousUser
    void getByNumber_noAccess() throws Exception {
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class))).thenReturn(Optional.empty());

        var employeeNumber = "20240101001";
        mockMvc.perform(get("/api/employees/" + employeeNumber).contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isUnauthorized());

        verify(employeeRepositoryAdapterMock, never()).getEmployeeByNumber(any(EmployeeNumber.class));
    }

    @Test
    @WithMockUser(authorities = "crud:kwops")
    void add() throws Exception {
        var employeeNumber = "20240101001";
        var number = new EmployeeNumber(employeeNumber);
        var id = 123456;
        when(employeeRepositoryAdapterMock.addEmployee(any(Employee.class)))
                .thenReturn(Employee.builder()
                        .id((long) id)
                        .number(number)
                        .firstName("John")
                        .lastName("Doe")
                        .startDate(LocalDate.of(2024, 1, 1))
                        .endDate(null)
                        .build());
        var employeeCreateDto = EmployeeCreateDto.builder()
                .firstName("John")
                .lastName("Doe")
                .startDate(LocalDate.of(2024, 1, 1))
                .build();

        mockMvc.perform(post("/api/employees").contextPath(CONTEXT_PATH).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateDto))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("*://*/api/employees/" + id));

        verify(employeeRepositoryAdapterMock).addEmployee(any(Employee.class));
        verify(messageSenderMock).sendMessage(any(EmployeeHiredMessage.class));
    }

    @Test
    @WithMockUser(authorities = "read:kwops")
    void add_insufficientAuthorities() throws Exception {
        var employeeNumber = "20240101001";
        var number = new EmployeeNumber(employeeNumber);
        var id = 123456;
        when(employeeRepositoryAdapterMock.addEmployee(any(Employee.class)))
                .thenReturn(Employee.builder()
                        .id((long) id)
                        .number(number)
                        .firstName("John")
                        .lastName("Doe")
                        .startDate(LocalDate.of(2024, 1, 1))
                        .endDate(null)
                        .build());
        var employeeCreateDto = EmployeeCreateDto.builder()
                .firstName("John")
                .lastName("Doe")
                .startDate(LocalDate.of(2024, 1, 1))
                .build();

        mockMvc.perform(post("/api/employees").contextPath(CONTEXT_PATH).with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateDto))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isForbidden());

        verify(employeeRepositoryAdapterMock, never()).addEmployee(any(Employee.class));
        verify(messageSenderMock, never()).sendMessage(any(EmployeeHiredMessage.class));
    }

    @Test
    @WithMockUser(authorities = "crud:kwops")
    void dismiss() throws Exception {
        var employeeNumber = "20240101001";
        var employee = mock(Employee.class);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(post("/api/employees/" + employeeNumber + "/dismiss").contextPath(CONTEXT_PATH).with(csrf())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());

        verify(employee).dismiss(true);
    }

    @Test
    @WithMockUser(authorities = "read:kwops")
    void dismiss_insufficientAuthorities() throws Exception {
        var employeeNumber = "20240101001";
        var employee = mock(Employee.class);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(post("/api/employees/" + employeeNumber + "/dismiss").contextPath(CONTEXT_PATH).with(csrf())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isForbidden());

        verify(employee, never()).dismiss(true);
    }


    @Test
    @WithMockUser(authorities = "crud:kwops")
    void globalExceptionHandler_shouldReturnBadRequest() throws Exception {
        var employeeNumber = "20240101001";
        var employee = mock(Employee.class);
        var errorMessage = "Could not find employee with number: " + employeeNumber;
        var expectedError = new ErrorModel(errorMessage, 400);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenThrow(new ContractException(errorMessage));

        mockMvc.perform(post("/api/employees/" + employeeNumber + "/dismiss").contextPath(CONTEXT_PATH).with(csrf())
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedError)));

        verify(employee, never()).dismiss(anyBoolean());
    }

}
