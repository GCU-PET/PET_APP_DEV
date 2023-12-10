package com.example.pet;

//                            Gson gson = new Gson();
//                                    String jsonWifiData = gson.toJson(wifiData); // converting wifiData to JSON format
//
//                                    if (isNetworkAvailable()) {
//                                    new SendDataTask().execute(jsonWifiData); // passing the json string instead of String array
//                                    } else {
//                                    logView3.setText("Network connection not available");
//                                    }
//
//                                    List<String> shortestPath1 = classInfo.getShortestPath(starts, ends);
//        logView1.setText("1Shortest path from " + starts + " to " + ends + ": " + shortestPath1);

public interface OnTaskCompleted {
    void onTaskCompleted(String result);
}
