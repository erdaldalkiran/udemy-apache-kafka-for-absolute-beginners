import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class TransactionalProducer {

    public static void main(String[] args) {
        var props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "transactional-producer");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092, localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "transaction-id");

        var producer = new KafkaProducer<Integer, String>(props);

        //todo: to terminate previous transactions if any
        producer.initTransactions();

        producer.beginTransaction();
        System.out.println("writing transaction-1-messages");
        try {
            for (var i = 0; i < 2; i++) {
                producer.send(new ProducerRecord<>("transaction-topic-1", i, "transaction-1-message " + i));
                producer.send(new ProducerRecord<>("transaction-topic-2", i, "transaction-1-message " + i));
            }

            producer.commitTransaction();
            System.out.println("finishing transaction-1-messages");
        } catch (Exception ex) {
            producer.abortTransaction();
            System.out.println("error in transaction-1-messages");
            ex.printStackTrace();
        }

        System.out.println("writing transaction-2-messages");
        producer.beginTransaction();
        try {
            for (var i = 0; i < 2; i++) {
                producer.send(new ProducerRecord<>("transaction-topic-1", i, "transaction-2-message " + i));
                producer.send(new ProducerRecord<>("transaction-topic-2", i, "transaction-2-message " + i));
            }

            producer.abortTransaction();
            System.out.println("aborting transaction-2-messages");
        } catch (Exception ex) {
            producer.abortTransaction();
            System.out.println("error in transaction-2-messages");
            ex.printStackTrace();
        }
    }
}
