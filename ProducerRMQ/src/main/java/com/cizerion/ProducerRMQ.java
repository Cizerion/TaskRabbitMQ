package com.cizerion;

import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

public class ProducerRMQ {
    private boolean durableExchange;

    public ProducerRMQ(){
        this.durableExchange = true;
    }

    public ProducerRMQ(boolean durableExchange){
        this.durableExchange = durableExchange;
    }

    public void send(String exchange, String routingKey, String host, String msg) throws Exception {
        //creating connection
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(host);

        //connecting to a broker on a local machine and creating a channel to work
        try (Connection connection = factory.newConnection();
             Channel channel = connection.createChannel()) {

            //declare a durable (or not), non-autodelete exchange of "direct" type
            channel.exchangeDeclare(exchange,"direct", durableExchange);

            //add MessageProperties - to dont lose our messages
            channel.basicPublish(exchange, routingKey, MessageProperties.PERSISTENT_TEXT_PLAIN, msg.getBytes("UTF-8"));
            System.out.println("Sent >>> '" + msg + "'");
        }
    }
}
