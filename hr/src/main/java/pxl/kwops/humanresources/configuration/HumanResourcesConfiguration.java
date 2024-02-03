package pxl.kwops.humanresources.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import pxl.kwops.humanresources.boundary.EmployeeMapper;
import pxl.kwops.humanresources.boundary.data.EmployeeJpaRepository;
import pxl.kwops.humanresources.boundary.data.EmployeeRepositoryAdapterImpl;
import pxl.kwops.humanresources.business.EmployeeRepositoryAdapter;
import pxl.kwops.humanresources.business.EmployeeService;
import pxl.kwops.humanresources.business.EmployeeServiceImpl;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageSender;

@Configuration
public class HumanResourcesConfiguration {

    private final EmployeeJpaRepository employeeJpaRepository;
    private final EmployeeMapper employeeMapper;

    @Autowired
    public HumanResourcesConfiguration(EmployeeJpaRepository employeeJpaRepository, EmployeeMapper employeeMapper) {
        this.employeeJpaRepository = employeeJpaRepository;
        this.employeeMapper = employeeMapper;
    }

    @Bean
    public EmployeeMapper employeeMapper() {
        return employeeMapper;
    }

    @Bean
    public EmployeeRepositoryAdapter employeeRepositoryAdapter() {
        return new EmployeeRepositoryAdapterImpl(employeeJpaRepository, employeeMapper);
    }

    @Bean
    public EmployeeService employeeService(EmployeeRepositoryAdapter employeeRepositoryAdapter, MessageSender<EmployeeHiredMessage> messageSender) {
        return new EmployeeServiceImpl(employeeRepositoryAdapter, messageSender);
    }

}
