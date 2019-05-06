package com.cizerion;

public class ConsumerAPP {
    private final static String QUEUE_NAME = "orders";
    private final static String EXCHANGE_NAME = "postPoint";
    private final static String BINDING_KEY = "Y2019";
    private final static String HOST = "localhost";

    public static void main(String[] args) {
        ConsumerRMQ consumer = new ConsumerRMQ();
        try {
            consumer.recieve(EXCHANGE_NAME, BINDING_KEY, QUEUE_NAME, HOST);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
