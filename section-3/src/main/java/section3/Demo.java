package section3;

import java.util.Properties;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

public class Demo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        var props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "section-3-demo");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        var producer = new KafkaProducer<Integer, String>(props);

        System.out.println("sending messages!");
        for (int i = 0; i < 500000; i++) {
            var record = producer.send(new ProducerRecord<>("invoice", i, "Simple Message" + i)).get();
            if (i % 1000 == 0) {
                System.out.println("" + i + "  messages have been send!");
            }
        }

        producer.close();
        System.out.println("all messages have been send!");
    }
}
