package com.wezaam.withdrawal.processor.event;

import com.wezaam.withdrawal.withdrawal.WithdrawalScheduled;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Slf4j
@Component
public class EventServiceRetry {

    private final BlockingQueue<String> inMemoryQueue = new LinkedBlockingQueue<>();

    @Resource
    private FailedEventRepository failedEventRepository;

    @PostConstruct
    public void initConsumer() {
        // Simulate a background consumer thread
        Thread consumerThread = new Thread(() -> {
            while (true) {
                try {
                    String message = inMemoryQueue.take();
                    log.info("Consumed event from queue: {}", message);
                    // Simulate processing
                    Thread.sleep(500);
                    log.info("Successfully processed event: {}", message);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Consumer interrupted", e);
                }
            }
        });
        consumerThread.setDaemon(true);
        consumerThread.start();
    }


    /**
     * Retries send 3 times then fall-back to recover()
     *
     * @param withdrawal the scheduled withdrawal for which an event should be sent
     */
    @Retryable
    public void sendWithRetries(WithdrawalScheduled withdrawal) {
        log.info("sendWithRetries send with withdrawal: {}", withdrawal);

        if (Math.random() < 0.3) {
            throw new RuntimeException("Simulated random failure while sending message.");
        }

        // Emulate sending to queue
        String message = "Event for withdrawal: " + withdrawal.getId();
        inMemoryQueue.offer(message);
        log.info("Published event to in-memory queue: {}", message);
    }

    /**
     * Callback method executed when the send operation repeatedly fails after retries.
     * <p>
     * Logs the error and saves the failed event into persistence for follow-up actions.
     * </p>
     *
     * @param e          the exception thrown during event processing
     * @param withdrawal the withdrawal linked to the failed event
     */
    @Recover
    public void recover(Exception e, WithdrawalScheduled withdrawal) {
        log.error("Failed to send withdrawal event after retries: {}", withdrawal, e);
        failedEventRepository.save(new FailedEvent(null, withdrawal, e.getMessage()));
    }


}
