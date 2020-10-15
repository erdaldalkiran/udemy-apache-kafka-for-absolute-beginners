import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class BasicProducer {
    public static void main(String[] args) {
        var props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "basic-producer-example");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var producer = new KafkaProducer<Integer, String>(props);

        for (int i = 0; i < 1_000_000; i++) {
            producer.send(new ProducerRecord<Integer, String>("hello-producer-topic", i, "Simple message " + i));
            if(i%1000 == 0){
                System.out.println(i+ " messages have been send!");
            }
        }
        producer.close();
    }

}
