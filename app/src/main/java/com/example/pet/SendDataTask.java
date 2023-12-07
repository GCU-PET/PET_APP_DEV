package com.example.pet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;


public class SendDataTask extends AsyncTask<String, Void, String> {
    private final OnTaskCompleted listener;

    public SendDataTask(OnTaskCompleted listener) {
        this.listener = listener;
    }



    @Override
    protected String doInBackground(String... strings) {
        try {
            // JSON 배열 생성
            JSONArray jsonArray = new JSONArray(strings[0]);

            // 각 요소를 변수로 분리
            String text = jsonArray.getString(0);
            String method = jsonArray.getString(1);
            String endpoint = jsonArray.getString(2);

            // 변수 출력
            System.out.println("Text: " + text);
            System.out.println("Method: " + method);
            System.out.println("Endpoint: " + endpoint);

            String urlString = "https://f594-183-98-101-163.ngrok-free.app/" + endpoint;
            URL url = new URL(urlString);
            System.out.println(url);
            System.out.println(method);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setUseCaches(false);
            conn.setRequestProperty("Content-Type", "application/json"); // UTF-8 인코딩 설정
            conn.setRequestProperty("ngrok-skip-browser-warning", "69420");

            // 요청 데이터 전송
            if (method.equals("POST")){
                System.out.println(url);
                // 요청 데이터 생성
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("title", text);
                System.out.println(jsonParam);
                System.out.println(jsonParam.toString().getBytes("UTF-8"));
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(String.valueOf(jsonParam));
                writer.flush();
                writer.close();
                os.close();

            }
            Log.e("server", "성공");
            // 응답 데이터 수신
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // UTF-8 인코딩으로 읽기
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();

//            return response.toString();

            int responseCode = conn.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 응답 데이터 읽기
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();

                // 응답 데이터 출력
                System.out.println("Response Data: " + response.toString());
                return response.toString();
            } else {
                System.out.println("API 요청 실패");
            }
            // 에러 응답인 경우
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
                // 에러 응답 데이터 읽기
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorInputLine;
                StringBuilder errorResponse = new StringBuilder();

                while ((errorInputLine = errorReader.readLine()) != null) {
                    errorResponse.append(errorInputLine);
                }

                errorReader.close();

                // 에러 응답 데이터 출력
                System.out.println("Error Response Data: " + errorResponse.toString());
            } else {
                // 성공한 경우 등에 대한 처리
                // ...
            }

            // 연결 닫기
            conn.disconnect();
            return Integer.toString(responseCode);


        } catch (JSONException e) {
            e.printStackTrace();
            return "Network request failed: " + e.getMessage();
        } catch (ProtocolException e) {
            throw new RuntimeException(e);
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


//        try {
//            String urlString = "http://172.16.101.2:8080/" + url1;
//            URL url = new URL(urlString);
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setRequestMethod(method);
//            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8"); // UTF-8 인코딩 설정
//            conn.setDoOutput(true);
//
//            // 요청 데이터 생성
//            JSONObject jsonParam = new JSONObject();
//            jsonParam.put("data", params[0]);
//
//            Log.d("wifi", jsonParam.toString());
//
//            // 요청 데이터 전송
//            OutputStream os = conn.getOutputStream();
//            os.write(jsonParam.toString().getBytes("UTF-8"));
//            os.flush();
//            os.close();
//
//            Log.e("server", "성공");
//            // 응답 데이터 수신
//            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8")); // UTF-8 인코딩으로 읽기
//            StringBuilder response = new StringBuilder();
//            String line;
//            while ((line = br.readLine()) != null) {
//                response.append(line);
//            }
//            br.close();
//
//            return response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "Network request failed: " + e.getMessage();
//        }
//        return "true";
    }


    @Override
    protected void onPostExecute(String result) {
        if (result != null) {
            // result를 활용하여 작업 수행
            Log.e("Result", "Result received: " + result);

            // 예시: "true" 문자열과 비교
            if ("true".equals(result)) {
                Log.e("test", "true");
            } else {
                Log.e("test", "false");
            }

            if (listener != null) {
                listener.onTaskCompleted(result);
            }

        }

    }
}
