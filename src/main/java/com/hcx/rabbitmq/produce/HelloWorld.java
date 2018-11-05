package com.hcx.rabbitmq.produce;


import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class HelloWorld<main> {
    private static final String QUEUE_NAME = "hello";

    static Channel channel = null;
    static Connection connection = null;
    static {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");

        try {
            connection = factory.newConnection();
            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }

    }

    public void getConnection() throws IOException {
        String message = "Hello World";
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println(" [x] Sent '" + message + "'");

    }

    public  void  consumer() throws IOException {
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body,"UTF-8");
                System.out.printf("[consumer]: "+message);
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);

    }

    public static void main(String[] args) throws IOException {
        HelloWorld helloWorld = new HelloWorld();
        helloWorld.getConnection();
        helloWorld.consumer();

    }

}
