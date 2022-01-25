package com.example.sendmoneyapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "payments")
public class Payment {
    @Id
    private String id;

    private String clientPhonenumber;
    private Double amount;
    private String remarks;
    private String occasion;
    private String paymentResponse;

    public String getClientPhonenumber() {
        return clientPhonenumber;
    }

    public void setClientPhonenumber(String clientPhonenumber) {
        this.clientPhonenumber = clientPhonenumber;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getOccasion() {
        return occasion;
    }

    public void setOcccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getPaymentResponse() {
        return paymentResponse;
    }

    public void setPaymentResponse(String paymentResponse) {
        this.paymentResponse = paymentResponse;
    }

}
