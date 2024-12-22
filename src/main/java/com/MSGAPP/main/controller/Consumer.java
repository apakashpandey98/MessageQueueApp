package com.MSGAPP.main.controller;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Consumer implements Runnable {
    private static final Logger logger = Logger.getLogger(Consumer.class.getName());
    private final BlockingQueue<String> messageQueue;

    public Consumer(BlockingQueue<String> queue) {
        this.messageQueue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String message = messageQueue.take();  // Take message from the queue
                logger.info("Consuming: " + message);
                MessageQueueApp.incrementSuccessCount();  // Successfully processed the message

                // Simulate error in processing if message contains "error"

                if (message.contains("error")) {
                    throw new RuntimeException("Simulated error while processing message: " + message);
                }

            } catch (InterruptedException e) {
                logger.severe("Consumer interrupted while taking message: " + e.getMessage());
                MessageQueueApp.incrementErrorCount();
                break;  // Exit if consumer is interrupted
            } catch (RuntimeException e) {
                logger.severe("Error processing message: " + e.getMessage());
                MessageQueueApp.incrementErrorCount();
            }
        }
    }
}
