package com.example.pet;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class FragmentTimeLine extends Fragment {

    private ArrayList<TimeLineModel> mDataList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_time_line, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.timeline_recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        setTimelineItems();

        recyclerView.setAdapter(new TimeLineViewAdapter(getContext(), mDataList));

        return view;
    }

    private void setTimelineItems() {
        mDataList.add(new TimeLineModel("0:00"));
        mDataList.add(new TimeLineModel("1:00"));
        mDataList.add(new TimeLineModel("2:00"));
        mDataList.add(new TimeLineModel("3:00"));
        mDataList.add(new TimeLineModel("4:00"));
        mDataList.add(new TimeLineModel("5:00"));
        mDataList.add(new TimeLineModel("6:00"));
        mDataList.add(new TimeLineModel("7:00"));
        mDataList.add(new TimeLineModel("8:00"));
        mDataList.add(new TimeLineModel("9:00"));
        mDataList.add(new TimeLineModel("10:00"));
    }


}