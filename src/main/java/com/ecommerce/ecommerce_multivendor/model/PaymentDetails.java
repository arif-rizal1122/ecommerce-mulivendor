package com.ecommerce.ecommerce_multivendor.model;

import com.ecommerce.ecommerce_multivendor.domain.PaymentStatus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    

    private String paymentId;

    private String razorpayPaymentLinkId;

    private String razorpayPaymentLinkReferenceId;

    private String razorpayPaymentIdZWSP;

    private PaymentStatus status;
}
