package com.example.pet;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
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
            String jsonData = jsonArray.getString(0);

            // 각 요소를 변수로 분리
            String text = jsonArray.getString(0);
            String method = jsonArray.getString(1);
            String endpoint = jsonArray.getString(2);

            // 변수 출력
            System.out.println("Text: " + text);
            System.out.println("Method: " + method);
            System.out.println("Endpoint: " + endpoint);

            String urlString = "https://wicked-paws-make.loca.lt/" + endpoint;
            URL url = new URL(urlString);
            System.out.println(url);
            System.out.println(method);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(method);
            conn.setUseCaches(false);

            if(jsonArray.length() == 4)
            {
                String token = jsonArray.getString(3);
                System.out.println("Token: " + token);
                conn.setRequestProperty("token", token); // UTF-8 인코딩 설정
            }

            conn.setRequestProperty("Content-Type", "application/json"); // UTF-8 인코딩 설정 //  multipart/form-data
            conn.setRequestProperty("ngrok-skip-browser-warning", "69420");

            // 요청 데이터 전송
            if (method.equals("POST")){
                System.out.println(url);
                // 요청 데이터 생성
                JSONObject jsonParam = new JSONObject();

                System.out.println(jsonParam.toString().getBytes("UTF-8"));
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(text);
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
                // 응답데이터가 오류일 경우 에러스트림을 읽어 해당 내용을 로깅해서 반환
                // onPostExecute에서 결과 확인하고 사용자에게 피드백
                System.out.println("API 요청 실패");

                //오류 처리
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
                String errorInputLine;
                StringBuilder errorResponse = new StringBuilder();

                while((errorInputLine =errorReader.readLine()) != null) {
                    errorResponse.append(errorInputLine);
                }

                errorReader.close();

                // 오류 응답 데이터 로깅 또는 반환
                Log.e("HTTP Error Response", errorResponse.toString());

                // 연결 끊기
                conn.disconnect();
                // 상태 코드와 함께 오류 메시지 반환
                return "Error - HTTP Code: " + responseCode + " Response: " + errorResponse.toString();
            }

//            // 에러 응답인 경우
//            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND) {
//                // 에러 응답 데이터 읽기
//                BufferedReader errorReader = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                String errorInputLine;
//                StringBuilder errorResponse = new StringBuilder();
//
//                while ((errorInputLine = errorReader.readLine()) != null) {
//                    errorResponse.append(errorInputLine);
//                }
//
//                errorReader.close();
//
//                // 에러 응답 데이터 출력
//                System.out.println("Error Response Data: " + errorResponse.toString());
//            } else {
//                // 성공한 경우 등에 대한 처리
//                // ...
//            }
//
//            // 연결 닫기
//            conn.disconnect();
//            return Integer.toString(responseCode);


        } catch (JSONException e) {
            e.printStackTrace();
            return "Network request failed: " + e.getMessage();
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
        super.onPostExecute(result);

            if (result != null) {
                // result를 활용하여 작업 수행
                Log.d("Result", "Result received: " + result);

//                // 예시: "true" 문자열과 비교
//                if ("success".equals(result)) {
//                    Log.e("test", "true");
//                } else {
//                    Log.e("test", "false");
//                }

                // 에러 메세지를 받았을 경우
                if (result.startsWith("Error - HTTP Code:")) {
                    // 오류 로직 처리, 예: 오류 메시지 파싱, 사용자에게 오류 알림 등
                    Log.e("onPostExecute", "Operation failed: " + result);

                    // 오류 메시지 추출 및 표시
                    String errorMessage = result.substring(result.indexOf("Response: ") + 10);

                    // 리스너를 통해 오류 결과 전달
                    if (listener != null) {
                        listener.onTaskCompleted(errorMessage);
                    }
                } else {
                    if (listener != null) {
                        listener.onTaskCompleted(result);
                    }
                }
            }  else {
                // 결과가 null인 경우의 처리
                Log.e("onPostExecute", "Received null response");
                listener.onTaskCompleted("No response received.");
            }

        }
}