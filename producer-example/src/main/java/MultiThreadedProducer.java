import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class MultiThreadedProducer {
    public static void main(String[] args) {
        var props = new Properties();
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "multi-threaded-producer-example");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092,localhost:9093");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        var producer = new KafkaProducer<Integer, String>(props);

        var threads = new ArrayList<Thread>();
        var directory = new File("data");
        for (var file : directory.listFiles()){
            var t = new Thread(new Dispatcher("hello-producer-topic", producer, file));
            threads.add(t);
            t.start();
        }
        try{
            for (var t : threads) t.join();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally {
            producer.close();
        }

        System.out.println("done");

    }

}
