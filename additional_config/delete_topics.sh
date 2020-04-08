docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic SOS-SENDER --force
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic SOS-RESPONSE --force
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic PERIODIC --force
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic ALERT --force
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --list
