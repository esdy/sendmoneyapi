package com.example.sendmoneyapi.services;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.minidev.json.JSONObject;

@Service

public class SecurityService {
    @Value("${spring.data.mpesa.short_code}")
    private String short_code;

    @Value("${spring.data.mpesa.lnm_passkey}")
    private String lnm_passkey;

    public String generateSecurity() throws UnsupportedEncodingException {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String pass = short_code + lnm_passkey + timeStamp;
        String password = Base64.getEncoder()
                .encodeToString(pass.getBytes("utf-8"));
        JSONObject json = new JSONObject();

        json.put("passowrd", password);
        json.put("timeStamp", timeStamp);

        return json.toString();
    }
}
