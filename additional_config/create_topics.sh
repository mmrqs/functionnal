docker-compose exec -T kafka kafka-topics --create --bootstrap-server \
localhost:9092 --replication-factor 1 --partitions 1 --topic SOS-SENDER
docker-compose exec -T kafka kafka-topics --create --bootstrap-server \
localhost:9092 --replication-factor 1 --partitions 1 --topic SOS-RESPONSE
docker-compose exec -T kafka kafka-topics --create --bootstrap-server \
localhost:9092 --replication-factor 1 --partitions 1 --topic PERIODIC
docker-compose exec -T kafka kafka-topics --create --bootstrap-server \
localhost:9092 --replication-factor 1 --partitions 1 --topic ALERT
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --list
