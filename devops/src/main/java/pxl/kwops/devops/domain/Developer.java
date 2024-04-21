package pxl.kwops.devops.domain;

import lombok.*;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.domain.models.Entity;

import java.util.List;
import java.util.UUID;

import static pxl.kwops.domain.utils.StringUtils.isNullOrEmpty;

@Getter
@Builder
@Setter
public class Developer extends Entity {

    private final String id;
    private final String firstName;
    private final String lastName;
    private final Percentage rating;
    private UUID teamId;

    private Developer(String id, String firstName, String lastName, Percentage rating, UUID teamId) {
        Contracts.require(!isNullOrEmpty(id), "The identifier of a developer cannot be empty");
        Contracts.require(!isNullOrEmpty(firstName), "The first name of a developer cannot be empty");
        Contracts.require(!isNullOrEmpty(lastName), "The last name of a developer cannot be empty");

        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.rating = rating;
        this.teamId = teamId;
    }

    @Override
    protected List<Object> getIdComponents() {
        return List.of(getId());
    }
}

