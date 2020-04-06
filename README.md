# functionnal

# Run the project

## Console part : 

1 - Go to `src_project` and enter :
```
./fetch.sh
```
or
```
bash ./fetch.sh
```

2 - enter : 

```
docker-compose up -d
```

To stop the docker containers just do : 

```
docker-compose stop
```
## Application part : 

Order for the run :

1. LauncherDrone1
2. ProducerTechnician
3. ConsumerTechnician
4. ConsumerBigBrother

The order for the last 3 modules does not matter. However, you must launch "LauncherDrone1" first.

# Kafka Channels

To create a topic enter the following command in your console : 

```
docker-compose exec -T kafka kafka-console-producer --broker-list kafka:9092 --topic [TOPIC_NAME]
```

List of the channels you must create in order to run the project : 

| TOPIC NAME                | DESCRIPTION                           |
| :------------------------:|:--------------------------------------| 
| SOS-SENDER                | Topic for messages sent by the drone when it can not qualify a possible offense with accuracy. These messages are sent to operators. |
| ALERT                     | Topic for messages in case of violation. | 
| PERIODIC                  | Topic for messages sent regularly by each drones. | 
| SOS-RESPONSE-[ID_DRONE]   | Topic for messages sent by an operator to send the right violation code to a specific drone which required his help|

# Organization of the code: 

## Drone 1

- LauncherDrone1 : launch 2 threads : Consumer and Producer
- Consumer : Receives the operator message and send it the Producer through the Bridge
- Producer : Send messages
- Bridge : allows the Consumer and the Producer to communicate

## Technician 

- ConsumerTechnician : receives help request messages from drones
- ProducerTechnician : responds to the request for help from a specific drone


## BigBrother

- ConsumerBigBrother : receives ALERT and PERIODIC messages

