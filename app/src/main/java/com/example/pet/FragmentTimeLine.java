package com.example.pet;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentTimeLine extends Fragment {

    private String date;

    public void setLogDate(String date) {
        this.date = date;
    }

    private ArrayList<TimeLineModel> mDataList = new ArrayList<>();

    private TimeLineViewAdapter timeLineViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);

        Log.i("Timeline DATE",date);

        RecyclerView recyclerView = view.findViewById(R.id.timeline_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        timeLineViewAdapter = new TimeLineViewAdapter(getContext(), mDataList);
        recyclerView.setAdapter(timeLineViewAdapter);

        setTimelineItems();

        return view;
    }

    private void setTimelineItems() {
        BoardServiceApi service = RetrofitClient.getTimelineLogList(getContext());
        Call<LogListResponse> call = service.getLogList(date);

        call.enqueue(new Callback<LogListResponse>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onResponse(Call<LogListResponse> call, Response<LogListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LogListResponse logListResponse = response.body();

                    if (logListResponse.isResult()) {
                        mDataList.clear();

                        if (logListResponse.getLogList() != null){
                            for (LogListResponse.LogItem logItem : logListResponse.getLogList()) {
                                // Convert LogItem to TimeLineModel
                                TimeLineModel timeLineModel = new TimeLineModel(logItem.getDate(), logItem.getStatus()); // Assuming no image data from server
                                mDataList.add(timeLineModel);
                            }
                        }
                        else {
                            Log.e("TIMELINE","loglistResponse가 비어있습니다.");
                        }
                        //mDataList.addAll(logListResponse.getLogList());

                        timeLineViewAdapter.notifyDataSetChanged();
                    } else {
                        Log.e(TAG, "Response Error :: Result is false");
                    }
                } else {
                    Log.e(TAG, "Request Error :: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<LogListResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });

//        mDataList.add(new TimeLineModel("0:00"));
//        mDataList.add(new TimeLineModel("1:00"));
//        mDataList.add(new TimeLineModel("2:00"));
//        mDataList.add(new TimeLineModel("3:00"));
//        mDataList.add(new TimeLineModel("4:00"));
//        mDataList.add(new TimeLineModel("5:00"));
    }

}