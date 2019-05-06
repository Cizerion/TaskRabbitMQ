package com.cizerion;

public class ProducerAPP {
    private final static String EXCHANGE_NAME = "postPoint";
    private final static String BINDING_KEY = "Y2019";
    private final static String HOST = "localhost";
    private final static String[] message = {"work", "hide", "sleep", "run", "dig", "listen", "talk"};

    //message generator
    private static String getMessage(){
        return message[(int)(Math.random() * 7)];
    }

    public static void main(String[] args) {
        int counter = 0;
        ProducerRMQ producer = new ProducerRMQ();
        try {
           while(counter != 20){
                producer.send(EXCHANGE_NAME, BINDING_KEY, HOST, getMessage());
                counter++;
                Thread.sleep(1000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
