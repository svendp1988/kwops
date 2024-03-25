package pxl.kwops.devops.domain;


import lombok.Builder;
import lombok.Getter;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.domain.models.Entity;

import java.util.*;

import static pxl.kwops.domain.utils.StringUtils.isNullOrEmpty;

@Getter
@Builder
public class Team extends Entity {
    private final UUID id;
    private final String name;
    private final List<Developer> developers;

    private Team(UUID id, String name, List<Developer> developers) {
        Contracts.require(!isNullOrEmpty(name), "A name must be provided when creating a new team.");

        this.id = id == null ? generateNonEmptyUUID() : id;
        this.name = name;
        this.developers = developers == null ? new ArrayList<>() : developers;
    }

    public void join(Developer developer) {
        Set<Developer> developerSet = new HashSet<>(developers);

        Contracts.require(developerSet.add(developer), "Cannot add the same developer to a team twice.");
        developer.setTeamId(id);
        developers.add(developer);
    }

    @Override
    protected List<Object> getIdComponents() {
        return List.of(getId());
    }

    private static UUID generateNonEmptyUUID() {
        Random random = new Random();
        UUID randomUUID;

        do {
            byte[] bytes = new byte[16];
            random.nextBytes(bytes);
            randomUUID = UUID.nameUUIDFromBytes(bytes);

        } while (randomUUID.equals(UUID.fromString("00000000-0000-0000-0000-000000000000")));

        return randomUUID;
    }

}
