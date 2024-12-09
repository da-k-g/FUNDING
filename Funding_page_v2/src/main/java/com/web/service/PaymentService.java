package com.web.service;

public interface PaymentService {
    String processPayment(Long projectId, int amount);
}
