package org.question_kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;
import org.apache.kafka.common.serialization.StringDeserializer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisConnectionException;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class MessageSpecialConsumer {

    public static void main(String[] args) {
        final String topicName = "userInput"; // Kafka topic name
        String groupId = "generalConsumers";
        long pollingTime = 100;

        Properties configProps = getProperties(groupId);

        try (KafkaConsumer<Integer, String> consumer = new KafkaConsumer<>(configProps); Jedis jedis = new Jedis()) {
            consumer.subscribe(List.of(topicName));
            System.out.println("Listening for messages...");
            while (true) {
                ConsumerRecords<Integer, String> consumerRecords = consumer.poll(Duration.ofMillis(pollingTime));
                for (ConsumerRecord<Integer, String> consumerRecord : consumerRecords) {
                    Thread.sleep(20000);
                    String hashedValue = doMD5HashCalculation(consumerRecord.value());
                    jedis.set(String.valueOf(consumerRecord.key()), hashedValue);
                    System.out.printf("Received message: key: %d, value: %s, Hashed Value: %s %n", consumerRecord.key(), consumerRecord.value(), hashedValue);
                }
            }
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException occurred");
        } catch (JedisConnectionException e) {
            System.out.println("Error when connecting to Redis !! : " + e);
        } catch (Exception e) {
            System.out.println("Error occurred !! : " + e);
        }
    }

    /**
     * Initializing Kafka Properties
     *
     * @param groupId : Specifies consumer group id
     * @return properties object with all the essential configurations
     */
    private static Properties getProperties(String groupId) {
        Properties configProps = new Properties();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configProps.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        configProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class.getName());
        configProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        configProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest"); // Reset to latest offset if no committed offset is found
        return configProps;
    }

    /**
     * @param input : Input string needed to be hashed
     * @return Hashed string
     * @throws NoSuchAlgorithmException : Raises if the specified algorithm (MD5) does not exist
     */
    private static String doMD5HashCalculation(String input) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(input.getBytes());
        byte[] resultByte = md.digest();
        // Convert the byte array to a hexadecimal string
        StringBuilder hexString = new StringBuilder();
        for (byte b : resultByte) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();

    }
}
