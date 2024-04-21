package pxl.kwops.devops.boundary.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "developers")
public class DeveloperEntity {

    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private double rating;
    private String teamId;

}
