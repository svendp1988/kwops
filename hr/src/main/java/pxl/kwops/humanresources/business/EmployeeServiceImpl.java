package pxl.kwops.humanresources.business;

import lombok.RequiredArgsConstructor;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final Employee.Factory employeeFactory = new Employee.Factory();
    private final EmployeeRepositoryAdapter employeeRepositoryAdapter;

    @Override
    public Employee hireNewEmployee(String lastName, String firstName, LocalDate startDate) {
        var numberOfStarters = employeeRepositoryAdapter.getNumberOfStartersOnStartTime(startDate);
        var newEmployee = employeeFactory.createNew(lastName, firstName, startDate, numberOfStarters + 1);

        return employeeRepositoryAdapter.addEmployee(newEmployee);
    }

    @Override
    public void dismissEmployee(EmployeeNumber employeeNumber, boolean withNotice) {
        var employeeToDismiss = employeeRepositoryAdapter.getEmployeeByNumber(employeeNumber);
        Contracts.require(employeeToDismiss.isPresent(), "Cannot find an employee with number '" + employeeNumber + "'");
        employeeToDismiss.get().dismiss(withNotice);
        employeeRepositoryAdapter.commitTrackedChanges();
    }

    @Override
    public Optional<Employee> getEmployeeByNumber(EmployeeNumber number) {
        return employeeRepositoryAdapter.getEmployeeByNumber(number);
    }

}
