package org.question_kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
import java.util.Scanner;

public class MessageProducer {
    public static void main(String[] args) {
        final String topicName = "userInput"; // Kafka-topic-name

        //configuring Kafka Producer
        Properties configProps = new Properties();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        try (Producer<Integer, String> producer = new KafkaProducer<>(configProps)) { //using Try with resources
            Scanner scanner = new Scanner(System.in);
            int keyCounter = 0;
            while (true) {
                System.out.print("Type exit to Exit | Enter Input String : ");
                String userInput = scanner.nextLine();
                if (userInput.equals("exit")) break;
                keyCounter++;
                ProducerRecord<Integer, String> producerRecord = new ProducerRecord<>(topicName, keyCounter, userInput);
                producer.send(producerRecord);
                System.out.printf("Message sent to topic %s with value %s and key %d%n", keyCounter, userInput, keyCounter);
            }
        }

    }

}
