package com.relatia.customer_service.notification;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling customer-related notification operations.
 */
@Service
@RequiredArgsConstructor
public class CustomerNotificationService {

    private final NotificationClient notificationClient;

    /**
     * Send a notification to a customer
     * @param customerId The ID of the customer
     * @param title The notification title
     * @param message The notification message
     * @param type The type of notification
     * @return The created notification
     */
    public NotificationResponse sendCustomerNotification(
            Long customerId,
            String title,
            String message,
            String type) {
        
        NotificationRequest request = NotificationRequest.builder()
                .recipientId(customerId)
                .title(title)
                .message(message)
                .type(type)
                .build();
                
        return notificationClient.createNotification(request);
    }

    /**
     * Get all notifications for a customer
     * @param customerId The ID of the customer
     * @return List of notifications
     */
    public List<NotificationResponse> getCustomerNotifications(Long customerId) {
        return notificationClient.getNotifications(customerId, false);
    }

    /**
     * Get unread notifications for a customer
     * @param customerId The ID of the customer
     * @return List of unread notifications
     */
    public List<NotificationResponse> getUnreadCustomerNotifications(Long customerId) {
        return notificationClient.getNotifications(customerId, true);
    }

    /**
     * Mark a notification as read
     * @param notificationId The ID of the notification
     * @return The updated notification
     */
    public NotificationResponse markNotificationAsRead(Long notificationId) {
        return notificationClient.markAsRead(notificationId);
    }

    /**
     * Delete a notification
     * @param notificationId The ID of the notification to delete
     */
    public void deleteNotification(Long notificationId) {
        notificationClient.deleteNotification(notificationId);
    }

    /**
     * Get notification count for a customer
     * @param customerId The ID of the customer
     * @param unreadOnly Whether to count only unread notifications
     * @return The count of notifications
     */
    public long getNotificationCount(Long customerId, boolean unreadOnly) {
        return notificationClient.getNotificationCount(customerId, unreadOnly);
    }
}
