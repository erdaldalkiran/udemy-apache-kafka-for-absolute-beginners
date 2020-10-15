export CONFLUENT_HOME=~/tools/kafka/confluent-6.0.0
export PATH=$PATH:$CONFLUENT_HOME/bin
export JAVA_HOME=$(/usr/libexec/java_home -v 11)
kafka-topics --create --zookeeper localhost:2181 --topic transaction-topic-2 --partitions 5 --replication-factor 3 --config min.insync.replicas=2