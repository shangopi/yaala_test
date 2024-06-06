package org.question_kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class MessageConsumer {
    public static void main(String[] args) {
        final String topicName = "userInput"; // Kafka topic name
        String groupId = "generalConsumers";
        long pollingTime = 100;

        Properties configProps = new Properties();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // Reset to latest offset if no committed offset is found

        try (KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(configProps)) {
            // Subscribe to topics
            consumer.subscribe(List.of(topicName));
            System.out.println("Listening for messages...");
            while (true) {
                ConsumerRecords<Integer, String> consumerRecords = consumer.poll(Duration.ofMillis(pollingTime));
                for (ConsumerRecord<Integer, String> consumerRecord : consumerRecords) {
                    System.out.printf("Received message: key: %d, value: %s%n", consumerRecord.key(), consumerRecord.value());
                }
            }
        }
    }
}
