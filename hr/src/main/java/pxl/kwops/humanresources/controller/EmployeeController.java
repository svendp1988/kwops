package pxl.kwops.humanresources.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pxl.kwops.humanresources.api.EmployeeControllerApi;
import pxl.kwops.humanresources.api.model.AllEmployees;
import pxl.kwops.humanresources.api.model.EmployeeCreateDto;
import pxl.kwops.humanresources.api.model.EmployeeDetailsDto;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.business.EmployeeService;

import static org.springframework.http.ResponseEntity.ok;

@RequiredArgsConstructor
@RestController
@RequestMapping("/employees")
public class EmployeeController implements EmployeeControllerApi {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @GetMapping
    public ResponseEntity<AllEmployees> getAllEmployees() {
        var allEmployees = AllEmployees.builder()
                .employees(employeeMapper.toEmployeeDetailsDtoList(employeeService.getAllEmployees()))
                .build();
        return ok(allEmployees);
    }

    @GetMapping("/{number}")
    public ResponseEntity<EmployeeDetailsDto> getByNumber(@PathVariable String number) {
        var employee = employeeService.getEmployeeByNumber(employeeMapper.mapToEmployeeNumber(number));

        return employee.map(value -> ok().body(employeeMapper.toEmployeeDetailsDto(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Void> add(@RequestBody EmployeeCreateDto model) {
        var hiredEmployee = employeeService.hireNewEmployee(model.getLastName(), model.getFirstName(), model.getStartDate());

        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest()
                        .path("/{id}")
                        .buildAndExpand(hiredEmployee.getId())
                        .toUri()).build();
    }

    @PostMapping("/{number}/dismiss")
    public ResponseEntity<Void> dismiss(@PathVariable String number, @RequestParam(defaultValue = "true", required = false) boolean withNotice) {
        employeeService.dismissEmployee(employeeMapper.mapToEmployeeNumber(number), withNotice);

        return ok().build();
    }
}


