package com.trusevichtot.websocket.config;

import com.trusevichtot.websocket.domain.model.event.Event;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import org.springframework.kafka.support.serializer.JsonDeserializer;

@Configuration
@EnableKafka
@Slf4j
public class Kafka {

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, Event>> kafkaListenerContainerFactory(ConsumerFactory<String, Event> consumerFactory) {
        var factory = new ConcurrentKafkaListenerContainerFactory<String, Event>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(10);
        factory.setBatchListener(true);
        factory.setMissingTopicsFatal(false);
        return factory;
    }

    @Bean
    public ConsumerFactory<String, Event> consumerFactory(KafkaProperties kafkaProperties) {
        return new DefaultKafkaConsumerFactory<>(kafkaProperties.buildConsumerProperties(), deserializer(new StringDeserializer()), deserializer(new JsonDeserializer<>(Event.class)));
    }

    @Bean
    public KafkaProperties consumerConfigs() {
        return new KafkaProperties();
    }

    private <T> Deserializer<T> deserializer(Deserializer<T> delegate) {
        var deserializer = new ErrorHandlingDeserializer<>(delegate);
        deserializer.setFailedDeserializationFunction((e) -> {
                    log.error("Deserialization is failed", e.getException());
                    return null;
                }
        );
        return deserializer;
    }
}
