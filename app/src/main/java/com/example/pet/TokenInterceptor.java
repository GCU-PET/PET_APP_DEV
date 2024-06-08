package com.example.pet;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // 토큰 파일에서 읽기
        String token = readTokenFromFile(context);

        if (token == null) {
            return chain.proceed(originalRequest);
        }

        // 새로운 요청 빌드
        Request.Builder builder = originalRequest.newBuilder()
                .header("token", token);
                //.header("Authorization", "Bearer " + token);

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }

    public String readTokenFromFile(Context context) {
        String token = null;
        try {
            File file = new File(context.getFilesDir(), "token.txt");
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            token = sb.toString();
            Log.i("token",token);
            br.close();
            isr.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
}
