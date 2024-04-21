package pxl.kwops.humanresources.domain;

import java.time.LocalDate;

public interface EmployeeFactory {
    Employee createNew(String lastName, String firstName, LocalDate startDate, int sequence);
}
