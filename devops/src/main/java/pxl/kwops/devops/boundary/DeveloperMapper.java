package pxl.kwops.devops.boundary;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pxl.kwops.devops.boundary.models.DeveloperEntity;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.devops.domain.Percentage;

import java.util.UUID;

import static pxl.kwops.domain.utils.StringUtils.isNullOrEmpty;


@Mapper(componentModel = "spring")
public interface DeveloperMapper {

    @Mapping(target = "rating", source = "rating.value")
    @Mapping(target = "teamId", source = "teamId", qualifiedByName = "mapFromTeamId")
    DeveloperEntity toDeveloperEntity(Developer developer);

    @Mapping(target = "rating", source = "rating", qualifiedByName = "mapToPercentage")
    @Mapping(target = "teamId", source = "teamId", qualifiedByName = "mapToTeamId")
    Developer toDeveloper(DeveloperEntity developerEntity);

    @Named("mapFromTeamId")
    default String mapFromTeamId(UUID value) {
        return value != null ? value.toString() : "";
    }

    @Named("mapToTeamId")
    default UUID mapToTeamId(String value) {
        return !isNullOrEmpty(value) ? UUID.fromString(value) : null;
    }

    @Named("mapToPercentage")
    default Percentage mapToPercentage(double value) {
        return new Percentage(value);
    }
}
