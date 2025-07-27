package com.relatia.customer_service.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomerNotificationController.class)
class CustomerNotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CustomerNotificationService notificationService;

    private NotificationResponse testNotification;
    private SendNotificationRequest sendRequest;

    @BeforeEach
    void setUp() {
        // Setup test notification
        testNotification = NotificationResponse.builder()
                .id(1L)
                .recipientId(1L)
                .title("Test Notification")
                .message("This is a test notification")
                .type("INFO")
                .read(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // Setup send request
        sendRequest = SendNotificationRequest.builder()
                .title("Test Notification")
                .message("This is a test notification")
                .type("INFO")
                .build();
    }

    @Test
    void getCustomerNotifications_ShouldReturnNotifications() throws Exception {
        // Given
        List<NotificationResponse> notifications = Arrays.asList(testNotification);
        when(notificationService.getCustomerNotifications(anyLong())).thenReturn(notifications);

        // When/Then
        mockMvc.perform(get("/api/v1/customers/1/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(testNotification.getId().intValue())))
                .andExpect(jsonPath("$[0].title", is(testNotification.getTitle())));
    }

    @Test
    void getUnreadCustomerNotifications_ShouldReturnUnreadNotifications() throws Exception {
        // Given
        List<NotificationResponse> notifications = Arrays.asList(testNotification);
        when(notificationService.getUnreadCustomerNotifications(anyLong())).thenReturn(notifications);

        // When/Then
        mockMvc.perform(get("/api/v1/customers/1/notifications/unread"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].read", is(false)));
    }

    @Test
    void getNotificationCount_ShouldReturnCount() throws Exception {
        // Given
        when(notificationService.getNotificationCount(anyLong(), anyBoolean())).thenReturn(5L);

        // When/Then
        mockMvc.perform(get("/api/v1/customers/1/notifications/count?unreadOnly=true"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void markAsRead_ShouldReturnUpdatedNotification() throws Exception {
        // Given
        testNotification.setRead(true);
        when(notificationService.markNotificationAsRead(anyLong())).thenReturn(testNotification);

        // When/Then
        mockMvc.perform(put("/api/v1/customers/1/notifications/1/read"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.read", is(true)));
    }

    @Test
    void deleteNotification_ShouldReturnNoContent() throws Exception {
        // When/Then
        mockMvc.perform(delete("/api/v1/customers/1/notifications/1"))
                .andExpect(status().isNoContent());

        verify(notificationService, times(1)).deleteNotification(1L);
    }

    @Test
    void sendNotification_ShouldCreateNotification() throws Exception {
        // Given
        when(notificationService.sendCustomerNotification(
                anyLong(), anyString(), anyString(), anyString()))
                .thenReturn(testNotification);

        // When/Then
        mockMvc.perform(post("/api/v1/customers/1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sendRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(testNotification.getId().intValue())))
                .andExpect(jsonPath("$.title", is(testNotification.getTitle())));
    }

    @Test
    void sendNotification_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Given
        SendNotificationRequest invalidRequest = new SendNotificationRequest();

        // When/Then
        mockMvc.perform(post("/api/v1/customers/1/notifications")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error", is("Bad Request")))
                .andExpect(jsonPath("$.status", is(400)))
                .andExpect(jsonPath("$.message", is("Validation failed")))
                .andExpect(jsonPath("$.path", is("/api/v1/customers/1/notifications")));
    }
}
