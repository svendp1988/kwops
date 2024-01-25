package pxl.kwops.humanresources.business;

import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeRepositoryAdapter {
    Employee addEmployee(Employee newEmployee);
    Optional<Employee> getEmployeeByNumber(EmployeeNumber number);
    int getNumberOfStartersOnStartTime(LocalDate startTime);
    void commitTrackedChanges();
}
