package com.relatia.customer_service.notification;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

/**
 * Fallback implementation for NotificationClient that provides fallback behavior
 * when the notification service is unavailable.
 */
@Slf4j
@Component
public class NotificationClientFallback implements NotificationClient {

    @Override
    public NotificationResponse createNotification(NotificationRequest request) {
        log.warn("Fallback: Could not create notification. Notification service is unavailable.");
        return NotificationResponse.builder()
                .message("Notification service is currently unavailable. Your notification will be processed when the service is back online.")
                .build();
    }

    @Override
    public NotificationResponse getNotification(Long id) {
        log.warn("Fallback: Could not retrieve notification with id {}. Notification service is unavailable.", id);
        return NotificationResponse.builder()
                .id(id)
                .message("Could not retrieve notification: Service unavailable")
                .build();
    }

    @Override
    public List<NotificationResponse> getNotifications(Long recipientId, boolean unread) {
        log.warn("Fallback: Could not retrieve notifications for recipient {}. Notification service is unavailable.", recipientId);
        return Collections.emptyList();
    }

    @Override
    public long getNotificationCount(Long recipientId, boolean unread) {
        log.warn("Fallback: Could not get notification count for recipient {}. Returning 0 as fallback.", recipientId);
        return 0L;
    }

    @Override
    public NotificationResponse markAsRead(Long id) {
        log.warn("Fallback: Could not mark notification {} as read. Notification service is unavailable.", id);
        return NotificationResponse.builder()
                .id(id)
                .message("Could not mark notification as read: Service unavailable")
                .build();
    }

    @Override
    public void deleteNotification(Long id) {
        log.warn("Fallback: Could not delete notification {}. Notification service is unavailable.", id);
        // No action needed for void return type
    }
}
