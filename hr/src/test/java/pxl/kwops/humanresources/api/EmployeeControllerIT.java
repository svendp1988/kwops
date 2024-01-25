package pxl.kwops.humanresources.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import pxl.kwops.api.models.ErrorModel;
import pxl.kwops.domain.exceptions.ContractException;
import pxl.kwops.humanresources.boundary.models.EmployeeCreateDto;
import pxl.kwops.humanresources.business.EmployeeRepositoryAdapter;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
class EmployeeControllerIT {

    private static final String CONTEXT_PATH = "/api";

    @MockBean
    private EmployeeRepositoryAdapter employeeRepositoryAdapterMock;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
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
                .withFirstName("John")
                .withLastName("Doe")
                .withStartDate(LocalDate.of(2024, 1, 1))
                .build();

        mockMvc.perform(post("/api/employees").contextPath(CONTEXT_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeCreateDto))
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isCreated())
                .andExpect(redirectedUrlPattern("*://*/api/employees/" + id));

        verify(employeeRepositoryAdapterMock).addEmployee(any(Employee.class));
    }

    @Test
    void dismiss() throws Exception {
        var employeeNumber = "20240101001";
        var employee = mock(Employee.class);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenReturn(Optional.of(employee));

        mockMvc.perform(post("/api/employees/" + employeeNumber + "/dismiss").contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isOk());

        verify(employee).dismiss(true);
    }

    @Test
    void globalExceptionHandler_shouldReturnBadRequest() throws Exception {
        var employeeNumber = "20240101001";
        var employee = mock(Employee.class);
        var errorMessage = "Could not find employee with number: " + employeeNumber;
        var expectedError = new ErrorModel(errorMessage, 400);
        when(employeeRepositoryAdapterMock.getEmployeeByNumber(any(EmployeeNumber.class)))
                .thenThrow(new ContractException(errorMessage));

        mockMvc.perform(post("/api/employees/" + employeeNumber + "/dismiss").contextPath(CONTEXT_PATH)
                        .accept(MediaType.parseMediaType("application/json")))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(objectMapper.writeValueAsString(expectedError)));

        verify(employee, never()).dismiss(anyBoolean());
    }

}
