package com.MSGAPP.main.controller;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

public class MessageQueueApp {

    private static final Logger logger = Logger.getLogger(MessageQueueApp.class.getName());
    private static final int QUEUE_CAPACITY = 10;
    private static final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(QUEUE_CAPACITY);

    public static int successCount = 0;
    public static int errorCount = 0;

    public static void main(String[] args) throws InterruptedException {
        // Start producer and consumer threads
        Thread producerThread = new Thread(new Producer(messageQueue));
        Thread consumerThread = new Thread(new Consumer(messageQueue));

        producerThread.start();
        consumerThread.start();

        // Wait for threads to finish
        producerThread.join();
        consumerThread.join();

        // Log the final message counts
        logger.info("Total messages processed successfully: " + successCount);
        logger.info("Total errors encountered: " + errorCount);
    }

    public static void incrementSuccessCount() {
        successCount++;
    }

    public static void incrementErrorCount() {
        errorCount++;
    }
}
