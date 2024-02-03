package pxl.kwops.devops.boundary.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import pxl.kwops.devops.boundary.models.DeveloperEntity;

import java.util.List;

public interface DeveloperJpaRepository extends JpaRepository<DeveloperEntity, Long> {
    List<DeveloperEntity> findByTeamIdIsNull();

    @Modifying
    @Query("update DeveloperEntity d set d.teamId = ?1 where d.id = ?2")
    void updateTeamId(String teamId, Long id);
}
