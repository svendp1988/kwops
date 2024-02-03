package pxl.kwops.devops.configuration;


import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pxl.kwops.devops.boundary.event.MessageReceiverImpl;
import pxl.kwops.devops.business.DeveloperRepositoryAdapter;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageReceiver;


@Configuration
public class MessageConfiguration {

    @Value("${kwops.message.topicexchangename}")
    private String topicExchangeName;

    @Value("${kwops.message.queuename}")
    private String queueName;

    private final CachingConnectionFactory cachingConnectionFactory;
    private final DeveloperRepositoryAdapter developerRepositoryAdapter;

    public MessageConfiguration(CachingConnectionFactory cachingConnectionFactory, DeveloperRepositoryAdapter developerRepositoryAdapter) {
        this.cachingConnectionFactory = cachingConnectionFactory;
        this.developerRepositoryAdapter = developerRepositoryAdapter;
    }

    @Bean
    public MessageReceiver<EmployeeHiredMessage> messageReceiver() {
        return new MessageReceiverImpl(developerRepositoryAdapter);
    }

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);

        return rabbitTemplate;
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }
    @Bean
    Binding binding(Queue queue, TopicExchange exchange, @Value("${kwops.message.routingkey}") String routingKey) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

    @Bean
    public Declarables createPostRegistrationSchema(Queue queue, TopicExchange exchange, Binding binding) {
        return new Declarables(
                queue,
                exchange,
                binding
        );
    }

}
