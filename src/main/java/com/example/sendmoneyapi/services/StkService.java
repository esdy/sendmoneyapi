package com.example.sendmoneyapi.services;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@Service
public class StkService {

    @Autowired
    private AuthorizationService authorizationService;

    @Value("${spring.data.mpesa.base_url}")
    private String base_url;

    @Value("${spring.data.mpesa.short_code}")
    private String short_code;

    // STK Push status
    public String stkStatus(String CheckoutRequestID) throws IOException, ParseException {
        String result = authorizationService.generateToken();
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        String access_token = (String) jsonObject.get("access_token");
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String passWord = "174379bfb279f9aa9bdbcf158e97dd71a467cd2e0c893059b10f78e6b72ada1ed2c919" + timeStamp;
        String password = Base64.getEncoder()
                .encodeToString(passWord.getBytes("utf-8"));

        json.put("BusinessShortCode", short_code);
        json.put("Password", password);
        json.put("Timestamp", timeStamp);
        json.put("CheckoutRequestID", CheckoutRequestID);

        RequestBody body = RequestBody.create(json.toString(), JSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(base_url + "/mpesa/stkpushquery/v1/query")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }
}
