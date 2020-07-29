package com.talk.demo.provider;

import com.alibaba.fastjson.JSON;
import com.talk.demo.dto.AccessTokenDto;
import com.talk.demo.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import javax.net.ssl.*;
import java.io.IOException;
import java.net.http.HttpClient;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

@Component
public class GithubProvider {

    private OkHttpClient avoidSSLVerify() {
        OkHttpClient client = null;
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public void checkServerTrusted(X509Certificate[] chain,
                                           String authType) throws CertificateException {
            }
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[0];
            }
        };

        try {
            sslContext.init(null, new TrustManager[] { tm }, null);
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        javax.net.ssl.SSLSocketFactory factory = sslContext.getSocketFactory();

        client = new OkHttpClient.Builder()
                .sslSocketFactory(factory, tm)
                .hostnameVerifier(new HostnameVerifier() {
                    @Override
                    public boolean verify(String arg0, SSLSession arg1) {
                        return true;
                    }
                }).build();
        return client;
    }

    public String getAccessToken(AccessTokenDto dto) {
        OkHttpClient client = avoidSSLVerify();
        final MediaType type
                = MediaType.get("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON.toJSONString(dto), type);
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();


        try (Response response = client.newCall(request).execute()) {
            String res =  response.body().string();
            String[] split = res.split("&");
            return split[0].substring(13);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public GithubUser getUser(String accesstoken) {
        OkHttpClient client = avoidSSLVerify();

        Request request = new Request.Builder()
                .url("https://api.github.com/user")
                .addHeader("Authorization", "token " + accesstoken)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            GithubUser githubUser = JSON.parseObject(string, GithubUser.class);

            return  githubUser;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
