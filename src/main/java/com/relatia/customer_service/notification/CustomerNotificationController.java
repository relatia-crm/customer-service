package com.relatia.customer_service.notification;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.relatia.customer_service.constants.GlobalConstants.CUSTOMER_NOTIFICATION_API;

/**
 * REST controller for managing customer notifications.
 */
@Tag(
    name = "Customer Notifications",
    description = "APIs for managing customer notifications"
)
@RestController
@RequestMapping(path=CUSTOMER_NOTIFICATION_API)
@RequiredArgsConstructor
public class CustomerNotificationController {

    private final CustomerNotificationService notificationService;

    @Operation(summary = "Get all notifications for a customer")
    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getCustomerNotifications(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(notificationService.getCustomerNotifications(customerId));
    }

    @Operation(summary = "Get unread notifications for a customer")
    @GetMapping("/unread")
    public ResponseEntity<List<NotificationResponse>> getUnreadCustomerNotifications(
            @PathVariable Long customerId) {
        return ResponseEntity.ok(notificationService.getUnreadCustomerNotifications(customerId));
    }

    @Operation(summary = "Get notification count for a customer")
    @GetMapping("/count")
    public ResponseEntity<Long> getNotificationCount(
            @PathVariable Long customerId,
            @RequestParam(required = false, defaultValue = "false") boolean unreadOnly) {
        return ResponseEntity.ok(notificationService.getNotificationCount(customerId, unreadOnly));
    }

    @Operation(summary = "Mark a notification as read")
    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long customerId,
            @PathVariable Long notificationId) {
        return ResponseEntity.ok(notificationService.markNotificationAsRead(notificationId));
    }

    @Operation(summary = "Delete a notification")
    @DeleteMapping("/{notificationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNotification(
            @PathVariable Long customerId,
            @PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
    }

    @Operation(summary = "Send a notification to a customer")
    @PostMapping
    public ResponseEntity<NotificationResponse> sendNotification(
            @PathVariable Long customerId,
            @Valid @RequestBody SendNotificationRequest request) {
        NotificationResponse response = notificationService.sendCustomerNotification(
            customerId,
            request.getTitle(),
            request.getMessage(),
            request.getType()
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
