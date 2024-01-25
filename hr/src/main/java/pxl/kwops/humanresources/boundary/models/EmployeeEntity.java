package pxl.kwops.humanresources.boundary.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;
import pxl.kwops.humanresources.domain.EmployeeNumber;

import java.time.LocalDate;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "employees")
public class EmployeeEntity {

    @Id
    @GeneratedValue
    private Long id;
    private String number;
    private String lastName;
    private String firstName;
    private LocalDate startDate;
    private LocalDate endDate;

}
