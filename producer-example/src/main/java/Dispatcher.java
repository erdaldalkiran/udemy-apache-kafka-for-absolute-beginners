import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Dispatcher implements Runnable {
    private final String topicName;
    private final KafkaProducer<Integer, String> producer;
    private final File file;

    public Dispatcher(String topicName, KafkaProducer<Integer, String> producer, File file) {
        this.topicName = topicName;
        this.producer = producer;
        this.file = file;
    }

    @Override
    public void run() {
        try {
            var lines = Files.lines(file.toPath());
            lines.forEach(l -> producer.send(new ProducerRecord<>(topicName, null, l)));
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("message send is done!");
    }
}
