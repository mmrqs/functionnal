docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic SOS-SENDER
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic SOS-RESPONSE
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic PERIODIC
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --delete --topic ALERT
docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092 --list
