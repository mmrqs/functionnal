# Install Kafka & Spark images

To install Kafka and Spark docker images, there are two files, `launch.sh` and `stop.sh`.

The first launches the images, the second stops it.

To run them, simply use `./launch.sh` or `sudo bash launch.sh`.

# Kafka shortcuts

|                               Original long-ass command                               |   Shortened command    | Shorter ! |
| :-----------------------------------------------------------------------------------: | :--------------------: | :-------: |
|                                    docker-compose                                     |           /            |    DC     |
|                                docker-compose exec -T                                 |           /            |   DCEX    |
| docker-compose exec -T kafka kafka-console-producer --bootstrap-server localhost:9092 | kafka-console-producer |    KCP    |
| docker-compose exec -T kafka kafka-console-consumer --bootstrap-server localhost:9092 | kafka-console-consumer |    KCC    |
|      docker-compose exec -T kafka kafka-topics --bootstrap-server localhost:9092      |      kafka-topics      |    KT     |

## If you don't have the flemme

To setup the shortcuts, follow me down the rabbit hole ! (or skip to the flemme subtitle)

In the repository, you will find the file `.kafka_aliases` ; copy it to your personal folder (`~/`) using the command `cp ./.kafka_aliases ~/`. 

Then, add the following lines to the file `~/.bashrc` :

```bash
if [ -f ~/.kafka_aliases ]; then
    . ~/.kafka_aliases
fi 
```

Finally, reload your profile using `source ~/.bashrc` to complete the setup.

And voilà! never again you will have to type those filthy long commands. Note that this will not add bonus points.

## If you have the flemme

If you trust me and my bash abilities (which you should neither never do) take this shit and run it

```bash
cp .kafka_aliases ~/ && echo "[ -f ~/.kafka_aliases ] && . ~/.kafka_aliases" >> ~/.bashrc && source ~/.bashrc
```

# Run the project

## Application part : 

Order for the run :

1. LauncherDrones
2. ProducerTechnician
3. ConsumerTechnician
4. ConsumerBigBrother

The order for the last 3 modules does not matter. However, you must launch "LauncherDrones" first.

# Kafka Topics

List of the channels you must create in order to run the project : 

| TOPIC NAME                | DESCRIPTION                           |
| :------------------------:|:--------------------------------------| 
| SOS-SENDER                | Topic for messages sent by the drone when it can not qualify a possible offense with accuracy. These messages are sent to operators. |
| ALERT                     | Topic for messages in case of violation. | 
| PERIODIC                  | Topic for messages sent regularly by each drones. | 
| SOS-RESPONSE              | Topic for messages sent by an operator to send the right violation code to a specific drone which required his help|

# Organization of the code: 

## Drones

- LauncherDrones : launch 2 threads : Bridge and Producer

## TriForce 

- Dispatcher : Receives the operator message and send it the right Producer through the Bridge
- Producer : Send messages
- Bridge : allows the Dispatcher and the Producer to communicate

## Technician 

- ConsumerTechnician : receives help request messages from drones
- ProducerTechnician : responds to the request for help from a specific drone


## BigBrother

- ConsumerBigBrother : receives ALERT and PERIODIC messages


