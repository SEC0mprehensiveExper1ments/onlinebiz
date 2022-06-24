package com.njustc.onlinebiz.test.service.project;

import com.google.gson.Gson;
import com.njustc.onlinebiz.common.model.EntrustDto;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;

@Component
public class EntrustDtoGetter {
  EntrustDto getEntrustDto(String entrustId) throws IOException {
    CookieManager cookieManager = new CookieManager();
    cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
    OkHttpClient okHttpClient =
        new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager)).build();
    Request login =
        new Request.Builder()
            .url(
                "http://124.222.168.27:8080/api/login/?userName=BackEndAdm1n&userPassword=123456Adm1n")
            .post(Util.EMPTY_REQUEST)
            .build();
    Request request =
        new Request.Builder()
            .url("http://124.222.168.27:8080/api/entrust/" + entrustId + "/get_dto")
            .build();
    okHttpClient.newCall(login).execute();
    ResponseBody responseBody = okHttpClient.newCall(request).execute().body();
    Gson gson = new Gson();
    return gson.fromJson(responseBody.string(), EntrustDto.class);
  }
}
