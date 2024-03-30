package pxl.kwops.humanresources.business;

import lombok.RequiredArgsConstructor;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageSender;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final Employee.Factory employeeFactory = new Employee.Factory();
    private final EmployeeRepositoryAdapter employeeRepositoryAdapter;
    private final MessageSender<EmployeeHiredMessage> messageSender;

    @Override
    public Employee hireNewEmployee(String lastName, String firstName, LocalDate startDate) {
        var numberOfStarters = employeeRepositoryAdapter.getNumberOfStartersOnStartTime(startDate);
        var newEmployee = employeeFactory.createNew(lastName, firstName, startDate, numberOfStarters + 1);
        Employee employee = employeeRepositoryAdapter.addEmployee(newEmployee);
        messageSender.sendMessage(new EmployeeHiredMessage(employee.getNumber().toString(), employee.getFirstName(), employee.getLastName()));

        return employee;
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

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepositoryAdapter.getAllEmployees();
    }
}
