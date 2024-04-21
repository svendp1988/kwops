package pxl.kwops.humanresources.business;

import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface EmployeeService {
    Employee hireNewEmployee(String lastName, String firstName, LocalDate startDate);
    void dismissEmployee(EmployeeNumber employeeNumber, boolean withNotice);
    Optional<Employee> getEmployeeByNumber(EmployeeNumber number);
    List<Employee> getAllEmployees();
}
