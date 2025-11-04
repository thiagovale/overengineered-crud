package com.example.backend.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue.logs}")
    private String logsQueue;

    @Value("${rabbitmq.exchange.logs}")
    private String logsExchange;

    @Value("${rabbitmq.routing-key.logs}")
    private String logsRoutingKey;

    @Bean
    public Queue logsQueue() {
        return new Queue(logsQueue, true);
    }

    @Bean
    public TopicExchange logsExchange() {
        return new TopicExchange(logsExchange);
    }

    @Bean
    public Binding logsBinding(Queue logsQueue, TopicExchange logsExchange) {
        return BindingBuilder.bind(logsQueue).to(logsExchange).with(logsRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}