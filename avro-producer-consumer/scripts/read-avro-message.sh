export CONFLUENT_HOME=~/tools/kafka/confluent-6.0.0
export PATH=$PATH:$CONFLUENT_HOME/bin
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
kafka-avro-console-consumer --topic avro-topic --from-beginning --bootstrap-server localhost:9092 --property schema.registry.url=http://localhost:8081

