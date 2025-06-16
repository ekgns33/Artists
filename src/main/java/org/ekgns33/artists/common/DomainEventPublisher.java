package org.ekgns33.artists.common;

import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
public class DomainEventPublisher {

    private final ApplicationEventPublisher publisher;

    /** 즉시(ApplicationEventMulticaster) 발행  */
    public void publish(Object event) {
        publisher.publishEvent(event);
    }

    /** 현재 트랜잭션이 있다면 COMMIT 후 발행, 없으면 즉시 발행 */
    public void publishAfterCommit(Object event) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        publisher.publishEvent(event);
                    }
                }
            );
        } else {
            publish(event);
        }
    }
}
