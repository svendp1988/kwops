package pxl.kwops.humanresources.boundary.data;

import lombok.RequiredArgsConstructor;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.boundary.models.EmployeeEntity;
import pxl.kwops.humanresources.business.EmployeeRepositoryAdapter;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeRepositoryAdapterImpl implements EmployeeRepositoryAdapter {

    private final EmployeeJpaRepository employeeRepository;
    private final EmployeeMapper employeeMapper;

    @Override
    public Employee addEmployee(Employee newEmployee) {
        EmployeeEntity entity = employeeRepository.save(employeeMapper.toEmployeeEntity(newEmployee));

        return employeeMapper.toEmployee(entity);
    }

    @Override
    public Optional<Employee> getEmployeeByNumber(EmployeeNumber number) {
        return employeeRepository.findEmployeeByNumber(employeeMapper.mapFromEmployeeNumber(number))
                .map(employeeMapper::toEmployee);
    }

    @Override
    public int getNumberOfStartersOnStartTime(LocalDate startTime) {
        return employeeRepository.countEmployeesByStartDateEquals(startTime);
    }

    @Override
    public void commitTrackedChanges() {
        employeeRepository.flush();
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employeeMapper::toEmployee)
                .toList();
    }
}
