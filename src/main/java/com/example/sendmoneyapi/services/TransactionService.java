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
public class TransactionService {

    @Value("${spring.data.mpesa.base_url}")
    private String base_url;

    @Autowired
    private AuthorizationService authorizationService;

    // transaction status
    public String transactionStatus(String transactionID, String occasion) throws IOException, ParseException {

        String result = authorizationService.generateToken();
        JSONParser jsonParser = new JSONParser();

        JSONObject jsonObject = (JSONObject) jsonParser.parse(result);
        String access_token = (String) jsonObject.get("access_token");

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();

        json.put("Initiator", "testapi");
        json.put("SecurityCredential",
                "dVak4HTyJ3Q+WNbeTYkPEf+qq28RdlJ/soOFgt1o3PE+lh+COpr+4FonNxzsQJJZmfSV4KlFso5dmEjS1ZC/Y7RS68GAMc/UBMzB2dJJ+XO8yc4h0Qf9YPNx5SsUdSokDwSO+JDNy21mmhqB27MzDfElHv7epffUFyFglWQvDOGBLa6ifTDxrf2wdNiLxYT2qFzY7QxUNfeTz/q+1j2HqNB4CKk5gQFXOxbckRrp4qFonWK8hUZvoVXbFuLs5S6vOgGgNRo2jhNfq/7J3puhl7eAa4t/ONLw5TiVO1EB3uAwpPrikBHMt4sadzqu0fjjG2Gzkm7sKYXWhig/bq+g2Q==");
        json.put("CommandID", "TransactionStatusQuery");
        json.put("TransactionID", transactionID);
        json.put("PartyA", "600995");
        json.put("IdentifierType", "MSISDN");
        json.put("ResultURL", "https://dwanjala.com/callback.php");
        json.put("QueueTimeOutURL", "https://dwanjala.com/");
        json.put("Remarks", "Remarkable");
        json.put("Occasion", occasion);

        RequestBody body = RequestBody.create(json.toString(), JSON);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(base_url + "/mpesa/transactionstatus/v1/query")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + access_token)
                .build();

        try (Response response = client.newCall(request).execute()) {

            return response.body().string();
        }
    }

}
