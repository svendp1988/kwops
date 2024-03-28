package pxl.kwops.humanresources.boundary;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;
import pxl.kwops.humanresources.api.model.EmployeeDetailsDto;
import pxl.kwops.humanresources.boundary.models.EmployeeEntity;
import pxl.kwops.humanresources.domain.Employee;
import pxl.kwops.humanresources.domain.EmployeeNumber;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "number", source = "number", qualifiedByName = "mapFromEmployeeNumber")
    EmployeeDetailsDto toEmployeeDetailsDto(Employee employee);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "number", source = "number", qualifiedByName = "mapFromEmployeeNumber")
    EmployeeEntity toEmployeeEntity(Employee employee);

    @Mapping(target = "number", source = "number", qualifiedByName = "mapToEmployeeNumber")
    Employee toEmployee(EmployeeEntity employeeEntity);

    @Named("mapFromEmployeeNumber")
    default String mapFromEmployeeNumber(EmployeeNumber value) {
        return value != null ? value.toString() : "";
    }

    @Named("mapToEmployeeNumber")
    default EmployeeNumber mapToEmployeeNumber(String value) {
        return value != null && !value.isEmpty() ? new EmployeeNumber(value) : null;
    }

}
