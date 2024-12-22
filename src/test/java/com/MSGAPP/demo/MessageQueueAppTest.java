package com.MSGAPP.demo;

import com.MSGAPP.main.controller.Consumer;
import com.MSGAPP.main.controller.MessageQueueApp;
import com.MSGAPP.main.controller.Producer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MessageQueueAppTest {

    private static final Logger logger = Logger.getLogger(MessageQueueApp.class.getName());
    private static final BlockingQueue<String> messageQueue = new ArrayBlockingQueue<>(10);

    @BeforeEach
    public void setup() {
        // Reset the counts and logging setup before each test
        ConsoleHandler consoleHandler = new ConsoleHandler();
        logger.addHandler(consoleHandler);
        logger.setLevel(Level.ALL);
    }

    @Test
    public void testSuccessfulProcessing() throws InterruptedException {
        // Simulate successful message processing
        Thread producerThread = new Thread(new Producer(messageQueue));
        Thread consumerThread = new Thread(new Consumer(messageQueue));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        // Check that all messages were processed successfully
        assertEquals(20, MessageQueueApp.successCount);  // 20 messages should be processed
        assertEquals(0, MessageQueueApp.errorCount);    // No errors should occur
    }

    @Test
    public void testErrorHandlingInConsumer() throws InterruptedException {
        // Simulate an error while consuming certain messages
        Thread producerThread = new Thread(new ErrorProducer(messageQueue));
        Thread consumerThread = new Thread(new Consumer(messageQueue));

        producerThread.start();
        consumerThread.start();

        producerThread.join();
        consumerThread.join();

        // 10 messages should be successfully processed
        // 10 error messages should be encountered
        assertEquals(10, MessageQueueApp.successCount);  // Only 10 messages should be successfully processed
        assertEquals(10, MessageQueueApp.errorCount);    // 10 errors should occur due to error messages
    }

    static class ErrorProducer implements Runnable {
        private final BlockingQueue<String> queue;

        public ErrorProducer(BlockingQueue<String> queue) {
            this.queue = queue;
        }

        @Override
        public void run() {
            try {
                // Simulating 20 messages, half with "error" to trigger error scenario
                for (int i = 0; i < 20; i++) {
                    String message;
                    if (i % 2 == 0) {
                        message = "Message-" + i;
                    } else {
                        message = "errorMessage-" + i;  // Introduce error scenario
                    }
                    queue.put(message);  // Add message to the queue
                }
            } catch (InterruptedException e) {
                logger.severe("ErrorProducer interrupted: " + e.getMessage());
            }
        }
    }
}
