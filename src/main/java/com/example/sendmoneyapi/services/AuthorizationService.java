package com.example.sendmoneyapi.services;

import java.io.IOException;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class AuthorizationService {
    @Value("${spring.data.mpesa.consumer_key}")
    private String consumer_key;

    @Value("${spring.data.mpesa.consumer_secret}")
    private String consumer_secret;

    @Value("${spring.data.mpesa.base_url}")
    private String base_url;

    // generate token
    public String generateToken() throws IOException {
        String url = base_url + "/oauth/v1/generate?grant_type=client_credentials";
        String key_secret = consumer_key + ":" + consumer_secret;
        String credential = Base64.getEncoder()
                .encodeToString(key_secret.getBytes("utf-8"));
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .header("Authorization", "Basic " + credential)
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }

    }
}
