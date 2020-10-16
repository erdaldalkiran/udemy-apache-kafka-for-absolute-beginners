package consumer;

import data.Delivery;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.IntegerDeserializer;

import java.time.Duration;
import java.util.Collections;
import java.util.Properties;

public class Consumer {
    public static void main(String[] args) {
        var props = new Properties();
        props.put(ConsumerConfig.CLIENT_ID_CONFIG, "avro-consumer");
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092, localhost:9093");
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "avro-consumer-group");
        props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, IntegerDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, true);

        var consumer = new KafkaConsumer<Integer, Delivery>(props);

        consumer.subscribe(Collections.singletonList("avro-topic"));
        while (true) {
            try {
                var records = consumer.poll(Duration.ofMillis(100));
                for (var record : records) {
                    var key = record.key();
                    var val = record.value();
                    System.out.printf("key: %d value: %s %n", key, val.toString());
                }
            } catch (Exception ex) {
                System.out.println("error when fetching records");
                ex.printStackTrace();
            }
        }

    }
}
