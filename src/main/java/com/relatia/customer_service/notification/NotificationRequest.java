package com.relatia.customer_service.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationRequest {
    private Long recipientId;
    private String title;
    private String message;
    private String type;
    private String actionUrl;
}
