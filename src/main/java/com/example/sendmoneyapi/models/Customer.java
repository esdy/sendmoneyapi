package com.example.sendmoneyapi.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
public class Customer {

    @Id
    private String id;

    private String customerPhonenumber;
    private String recipientPhonenumber;
    private String stkPush;

    public String getCustomerPhoneNumber() {
        return customerPhonenumber;
    }

    public void setCustomerPhonenumber(String customerPhonenumber) {
        this.customerPhonenumber = customerPhonenumber;
    }

    public String getRecipientPhonenumber() {
        return recipientPhonenumber;
    }

    public void setRecipientPhonenumber(String recipientPhonenumber) {
        this.recipientPhonenumber = recipientPhonenumber;
    }

    public String getStkPush(String stkPush) {
        return stkPush;
    }

    public void setStkPush(String stkPush) {
        this.stkPush = stkPush;
    }
}
