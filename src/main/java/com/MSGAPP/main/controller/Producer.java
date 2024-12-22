package com.MSGAPP.main.controller;

import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class Producer implements Runnable {
    private static final Logger logger = Logger.getLogger(Producer.class.getName());
    private final BlockingQueue<String> messageQueue;

    public Producer(BlockingQueue<String> queue) {
        this.messageQueue = queue;
    }

    @Override
    public void run() {
        for (int i = 0; i < 20; i++) {
            try {
                String message = "Message-" + i;
                logger.info("Producing: " + message);
                messageQueue.put(message);  // Add message to the queue
            } catch (InterruptedException e) {
                logger.severe("Producer interrupted while adding message: " + e.getMessage());
                MessageQueueApp.incrementErrorCount();
            }
        }
    }
}
