package pxl.kwops.humanresources.configuration;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pxl.kwops.humanresources.boundary.MessageSenderImpl;
import pxl.kwops.message.EmployeeHiredMessage;
import pxl.kwops.message.MessageSender;


@Configuration
public class MessageConfiguration {

    @Value("${kwops.message.topicexchangename}")
    private String topicExchangeName;

    @Value("${kwops.message.queuename}")
    private String queueName;

    @Value("${kwops.message.routingkey}")
    private String routingKey;

    private final CachingConnectionFactory cachingConnectionFactory;

    public MessageConfiguration(CachingConnectionFactory cachingConnectionFactory) {
        this.cachingConnectionFactory = cachingConnectionFactory;
    }

    @Bean
    MessageSender<EmployeeHiredMessage> messageSender(RabbitTemplate rabbitTemplate) {
        return new MessageSenderImpl(topicExchangeName, routingKey, rabbitTemplate);
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
    Binding binding(Queue queue, TopicExchange exchange) {
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
