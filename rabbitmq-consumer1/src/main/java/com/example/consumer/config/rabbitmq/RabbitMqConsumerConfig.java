package com.example.consumer.config.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.RetryCallback;
import org.springframework.retry.RetryContext;
import org.springframework.retry.RetryListener;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import javax.annotation.Resource;

/**
 * @Author songbo
 * @Date 2020/8/12 18:31
 * @Version 1.0
 */
@Slf4j
@Configuration
public class RabbitMqConsumerConfig {

    @Resource
    private RabbitProperties properties;

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setAddresses(properties.getHost() + ":" + properties.getPort());
        connectionFactory.setUsername(properties.getUsername());
        connectionFactory.setPassword(properties.getPassword());
        return connectionFactory;
    }

    @Bean(name = "autoAckRabbitListenerContainerFactory")
    public SimpleRabbitListenerContainerFactory autoAckRabbitListenerContainerFactory(@Qualifier("connectionFactory") ConnectionFactory connectionFactory) {
        // SimpleRabbitListenerContainerFactory??????????????????content_type???text???????????????????????????string?????????
        SimpleRabbitListenerContainerFactory containerFactory = new SimpleRabbitListenerContainerFactory();
        containerFactory.setConnectionFactory(connectionFactory);
        containerFactory.setConcurrentConsumers(2);
        containerFactory.setMaxConcurrentConsumers(5);
        containerFactory.setAcknowledgeMode(AcknowledgeMode.AUTO); // ??????ACK???AcknowledgeMode.MANUAL ????????????
        containerFactory.setMessageConverter(new Jackson2JsonMessageConverter());
        containerFactory.setChannelTransacted(true);
        containerFactory.setAdviceChain(
                RetryInterceptorBuilder
                        .stateless()
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .retryOperations(rabbitRetryTemplate())
                        .build()
        );
        return containerFactory;
    }

    public RetryTemplate rabbitRetryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();

        // ??????????????????????????????
        retryTemplate.registerListener(new RetryListener() {
            @Override
            public <T, E extends Throwable> boolean open(RetryContext retryContext, RetryCallback<T, E> retryCallback) {
                // ?????????????????? ?????????false?????????????????????
                return true;
            }

            @Override
            public <T, E extends Throwable> void close(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                // ??????????????????????????? ????????????????????? ???
            }

            @Override
            public <T, E extends Throwable> void onError(RetryContext retryContext, RetryCallback<T, E> retryCallback, Throwable throwable) {
                //  ?????? ????????????
                log.error("-----???{}???????????????", retryContext.getRetryCount());
            }
        });

        retryTemplate.setBackOffPolicy(backOffPolicyByProperties());
        retryTemplate.setRetryPolicy(retryPolicyByProperties());
        return retryTemplate;
    }

    public ExponentialBackOffPolicy backOffPolicyByProperties() {
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        long maxInterval = properties.getListener().getSimple().getRetry().getMaxInterval().getSeconds();
        long initialInterval = properties.getListener().getSimple().getRetry().getInitialInterval().getSeconds();
        double multiplier = properties.getListener().getSimple().getRetry().getMultiplier();
        // ????????????
        backOffPolicy.setInitialInterval(initialInterval * 1000);
        // ??????????????????
        backOffPolicy.setMaxInterval(maxInterval * 1000);
        // ????????????????????????
        backOffPolicy.setMultiplier(multiplier);
        return backOffPolicy;
    }

    public SimpleRetryPolicy retryPolicyByProperties() {
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy();
        retryPolicy.setMaxAttempts(properties.getListener().getSimple().getRetry().getMaxAttempts());
        return retryPolicy;
    }

}
