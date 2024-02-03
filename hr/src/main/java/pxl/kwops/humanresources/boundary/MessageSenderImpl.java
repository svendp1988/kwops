package pxl.kwops.humanresources.boundary;

import lombok.AllArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageSender;

@AllArgsConstructor
public class MessageSenderImpl implements MessageSender<EmployeeHiredMessage> {

    private String topicExchangeName;

    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void sendMessage(EmployeeHiredMessage message) {
        rabbitTemplate.convertAndSend(topicExchangeName, routingKey, message);
    }
}
