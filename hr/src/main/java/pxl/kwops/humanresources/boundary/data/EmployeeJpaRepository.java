package pxl.kwops.humanresources.boundary.data;

import org.springframework.data.jpa.repository.JpaRepository;
import pxl.kwops.humanresources.boundary.models.EmployeeEntity;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;
import java.util.Optional;

public interface EmployeeJpaRepository extends JpaRepository<EmployeeEntity, Long> {
    Optional<EmployeeEntity> findEmployeeByNumber(String number);
    int countEmployeesByStartDateEquals(LocalDate startDate);
}
