package com.example.sendmoneyapi.controllers;

import java.io.IOException;
import java.util.List;

import com.example.sendmoneyapi.models.Customer;
import com.example.sendmoneyapi.services.CustomerService;
import com.example.sendmoneyapi.services.PaymentService;
import com.example.sendmoneyapi.services.StkService;
import com.example.sendmoneyapi.services.TransactionService;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mpesa")
public class MpesaController {

    @Autowired
    private CustomerService customerService;

    // customer initiates STK Push
    @PostMapping("/send")
    public String stkPush(@RequestParam String mynumber, @RequestParam String phonenumber, @RequestParam Double amount)
            throws IOException, ParseException {

        return customerService.stkPush(mynumber, phonenumber, amount);
    }

    @Autowired
    private StkService stkService;

    @PostMapping("/stkstatus")
    public String stkStatus(@RequestParam String CheckoutRequestID) throws IOException, ParseException {
        return stkService.stkStatus(CheckoutRequestID);
    }

    @Autowired
    private TransactionService transactionService;

    @PostMapping("/transactionstatus")
    public String transactionStatus(@RequestParam String TransactionID, @RequestParam String Occasion)
            throws IOException, ParseException {
        return transactionService.transactionStatus(TransactionID, Occasion);
    }

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/businesstoclient")
    public String businessToClient(@RequestParam String clientPhonenumber, @RequestParam String remarks,
            @RequestParam String occasion, @RequestParam Double amount)
            throws IOException, ParseException {
        return paymentService.businessToClient(clientPhonenumber, remarks, occasion, amount);
    }

}
