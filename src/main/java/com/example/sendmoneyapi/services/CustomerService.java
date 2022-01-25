package com.example.sendmoneyapi.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import com.example.sendmoneyapi.models.Customer;
import com.example.sendmoneyapi.repositories.CustomerRepository;

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
public class CustomerService {
    @Autowired
    public CustomerRepository customerRepository;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${spring.data.mpesa.base_url}")
    private String base_url;

    @Value("${spring.data.mpesa.short_code}")
    private String short_code;

    // initiate STK Push
    public String stkPush(String mynumber, String phonenumber, Double amount)
            throws IOException, ParseException {
        String result = authorizationService.generateToken();
        JSONParser jsonParser = new JSONParser();

        // String security_object = securityService.generateSecurity();
        // JSONObject securityObject = (JSONObject) jsonParser.parse(security_object);

        // String timeStamp = (String) securityObject.get("timeStamp");
        // String password = (String) securityObject.get("password");

        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        String access_token = (String) jsonObject.get("access_token");
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String passWord = "174379bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919" + timeStamp;
        String password = Base64.getEncoder()
                .encodeToString(passWord.getBytes("utf-8"));

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();

        json.put("BusinessShortCode", short_code);
        json.put("Password", password);
        json.put("Timestamp", timeStamp);
        json.put("TransactionType", "CustomerPayBillOnline");
        json.put("Amount", amount);
        json.put("PartyA", mynumber);
        json.put("PartyB", short_code);
        json.put("PhoneNumber", mynumber);
        json.put("CallBackURL", "https://dwanjala.com/callback.php");
        json.put("AccountReference", "Douglas");
        json.put("TransactionDesc", "Douglas");

        RequestBody body = RequestBody.create(json.toString(), JSON);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(base_url + "/mpesa/stkpush/v1/processrequest")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {

            String stkpush_status = response.body().string();
            Customer customer = new Customer();
            customer.setCustomerPhonenumber(mynumber);
            customer.setRecipientPhonenumber(phonenumber);
            customer.setStkPush(stkpush_status);
            mongoTemplate.save(customer, "customers");

            return stkpush_status;
        }

    }

}
