package producer;

import data.Delivery;
import data.Item;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroSerializerConfig;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;

import java.time.Instant;
import java.util.Arrays;
import java.util.Properties;

public class Producer {
    public static void main(String[] args) {
        var props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "avro-producer");
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "avro-producer-transaction-id");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092, localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(KafkaAvroSerializerConfig.SCHEMA_REGISTRY_URL_CONFIG, "http://localhost:8081");
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        var producer = new KafkaProducer<Integer, Delivery>(props);
        Runtime.getRuntime().addShutdownHook(new Thread(producer::close, "Shutdown-thread"));

        producer.initTransactions();

        try {
            producer.beginTransaction();

            var delivery = Delivery.newBuilder()
                .setCreatedAt(Instant.now().toEpochMilli())
                .setId(1)
                .setName("name")
                .setPrice(10.13)
                .setItems(Arrays.asList(
                    Item.newBuilder().setId(1).setName("item-name-1").setQuantity(3).build(),
                    Item.newBuilder().setId(2).setName("item-name-2").setQuantity(2).build()
                ))
                .build();
            var message = new ProducerRecord<Integer, Delivery>("avro-topic", 1, delivery);
            producer.send(message);
            producer.commitTransaction();
        } catch (Exception ex) {
            producer.abortTransaction();
            ex.printStackTrace();
        }


    }
}
