package com.cizerion;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConsumerRMQ {
    private boolean durableExchange;
    private boolean durableQueue;
    private Connection connection;
    private Channel channel;

    public ConsumerRMQ(){
        this.durableExchange = true;
        this.durableQueue = true;
    }

    public ConsumerRMQ(boolean durableExchange, boolean durableQueue){
        this.durableExchange = durableExchange;
        this.durableQueue = durableQueue;
    }

    public void recieve(String exchange, String routingKey, String queue, String host) throws Exception {
        //creating connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        //connecting to a broker on a local machine and creating a channel to work
        //connection keeps open to listen messages
        connection = factory.newConnection();
        channel = connection.createChannel();

        //declare a durable (or not), non-autodelete exchange of "direct" type
        channel.exchangeDeclare(exchange,"direct", durableExchange);
        //declare the queue and send the message: durable - queue won't be lost even if RabbitMQ restarts
        channel.queueDeclare(queue, durableQueue, false, false, null);
        //bind the queue to the exchange with the given routing key
        channel.queueBind(queue, exchange, routingKey);

        System.out.println("--- Waiting for messages. To exit press CTRL+C");

        //accept only one unack-ed message at a time(not to give more than one message to a consumer at a time)
        channel.basicQos(1);

        //buffering the messages until we are ready to use them
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received <<< '" + message + "'");
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        //autoAck = false - to send a proper acknowledgment from the consumer
        channel.basicConsume(queue, false, deliverCallback, consumerTag -> { });
    }

    public void closeConnections(){
        try {
            channel.close();
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
