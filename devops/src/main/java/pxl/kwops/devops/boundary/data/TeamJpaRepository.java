package pxl.kwops.devops.boundary.data;

import org.springframework.data.jpa.repository.JpaRepository;
import pxl.kwops.devops.boundary.models.TeamEntity;
import java.util.UUID;

public interface TeamJpaRepository extends JpaRepository<TeamEntity, UUID> {
}
