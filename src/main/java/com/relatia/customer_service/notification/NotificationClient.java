package com.relatia.customer_service.notification;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.relatia.customer_service.constants.GlobalConstants.NOTIFICATION_API;

/**
 * Feign client for interacting with the Notification Service.
 */
@FeignClient(name = "notification-service", path = NOTIFICATION_API, fallback = NotificationClientFallback.class)
public interface NotificationClient {

    /**
     * Create a new notification
     * @param request The notification request
     * @return The created notification
     */
    @PostMapping
    NotificationResponse createNotification(@RequestBody NotificationRequest request);

    /**
     * Get notification by ID
     * @param id The ID of the notification
     * @return The notification details
     */
    @GetMapping("/{id}")
    NotificationResponse getNotification(@PathVariable("id") Long id);

    /**
     * Get notifications for a recipient
     * @param recipientId The ID of the recipient
     * @param unread Whether to filter unread notifications only
     * @return List of notifications
     */
    @GetMapping
    List<NotificationResponse> getNotifications(
            @RequestParam("recipientId") Long recipientId,
            @RequestParam(name = "unread", required = false, defaultValue = "false") boolean unread
    );

    /**
     * Get notification count
     * @param recipientId The ID of the recipient
     * @param unread Whether to count only unread notifications
     * @return The count of notifications
     */
    @GetMapping("/count")
    long getNotificationCount(
            @RequestParam("recipientId") Long recipientId,
            @RequestParam(name = "unread", required = false, defaultValue = "false") boolean unread
    );

    /**
     * Mark a notification as read
     * @param id The ID of the notification to mark as read
     * @return The updated notification
     */
    @PutMapping("/{id}/read")
    NotificationResponse markAsRead(@PathVariable("id") Long id);

    /**
     * Delete a notification
     * @param id The ID of the notification to delete
     */
    @DeleteMapping("/{id}")
    void deleteNotification(@PathVariable("id") Long id);
}
