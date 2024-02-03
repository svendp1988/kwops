package pxl.kwops.devops.boundary.event;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;
import pxl.kwops.devops.domain.Developer;
import pxl.kwops.domain.models.Contracts;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageReceiver;


@AllArgsConstructor
public class MessageReceiverImpl implements MessageReceiver<EmployeeHiredMessage> {

    private final DeveloperRepositoryAdapter developerRepositoryAdapter;
    private final Logger logger = LoggerFactory.getLogger(MessageReceiverImpl.class);

    @RabbitListener(queues = "${kwops.message.queuename}")
    public void receiveMessage(EmployeeHiredMessage message) {
        Contracts.require(message != null, "message must not be null");
        logger.info("DevOps - Handling employee hire. Id: " + message.getId());
        var dev = developerRepositoryAdapter.findById(message.getNumber());
        if (dev.isPresent()) {
            logger.info("DevOps - No developer added. A developer with id '" + message.getNumber() + "' already exists. Id: " + message.getId());
            return;
        }
        Developer developer = developerRepositoryAdapter.addDeveloper(Developer.builder()
                .id(message.getNumber())
                .firstName(message.getFirstName())
                .lastName(message.getLastName())
                .build());
        logger.info("DevOps - Developer with id '" + developer.getId() + "' added. Id: " + message.getId());
    }

}
