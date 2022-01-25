package com.example.sendmoneyapi.services;

import java.io.IOException;
import java.util.Base64;

import com.example.sendmoneyapi.models.Payment;
import com.example.sendmoneyapi.repositories.PaymentRepository;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class PaymentService {
    @Autowired
    public PaymentRepository paymentRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${spring.data.mpesa.base_url}")
    private String base_url;

    @Value("${spring.data.mpesa.short_code}")
    private String short_code;

    // business to client
    public String businessToClient(String clientPhonenumber, String remarks, String occasion, Double amount)
            throws IOException, ParseException {

        String result = authorizationService.generateToken();
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        String access_token = (String) jsonObject.get("access_token");

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();

        json.put("InitiatorName", "testapi");
        json.put("SecurityCredential",
                "fY8GlYoD1r/zN9rna3avEkaKlZrKbhpoStHzI9KqXgoVKW0Kjr4JOphAVAiUZqMBdp6+L+bC4EQIR8cMLkN18OAi9GH68Bjs8ahDYJXC4J8Cb6ldoElFlIy8xwNP/T49HCTYMUCYwKpLG/rWlEFaBs9kBHL1swMNrOxbkUoZb8FByT9BYBWBoQvBBpcflgaoX4ENCTMUDyuzk8T//jcr7f7tO3Bd0G12PpyFrtD+z+/jF4VfAdsU6V8VqGTs2SjusVFtZP2OFWdS6Z0VJtJh3kxqjS+E9OjlE48IA5FXm0NjN13/8kG7YMhaeqHufPr6ZH+EBPYyXtuy1Px+yBIpPw==");
        json.put("CommandID", "BusinessPayment");
        json.put("Amount", amount);
        json.put("PartyA", "600987");
        json.put("PartyB", clientPhonenumber);
        json.put("Remarks", remarks);
        json.put("QueueTimeOutURL", "https://dwanjala.com/");
        json.put("ResultURL", "https://dwanjala.com/callback.php");
        json.put("Occasion", occasion);

        RequestBody body = RequestBody.create(json.toString(), JSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(base_url + "/mpesa/b2c/v1/paymentrequest")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {

            String payment_response = response.body().string();
            Payment payment = new Payment();
            payment.setClientPhonenumber(clientPhonenumber);
            payment.setAmount(amount);
            payment.setRemarks(remarks);
            payment.setPaymentResponse(payment_response);
            mongoTemplate.save(payment, "payments");

            return payment_response;
        }

    }
}
