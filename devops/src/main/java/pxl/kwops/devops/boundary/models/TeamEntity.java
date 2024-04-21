package pxl.kwops.devops.boundary.models;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "teams")
public class TeamEntity {

    @Id
    @Generated
    private UUID id;
    private String name;
    @OneToMany(mappedBy = "teamId", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private List<DeveloperEntity> developers;

}
