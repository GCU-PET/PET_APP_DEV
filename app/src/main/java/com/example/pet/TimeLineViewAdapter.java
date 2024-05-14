package com.example.pet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vipulasri.timelineview.TimelineView;

import java.util.ArrayList;

public class TimeLineViewAdapter extends RecyclerView.Adapter<TimeLineViewAdapter.TimelineViewHolder> {
    private Context context;
    private ArrayList<TimeLineModel> timeLineModels;
    private LayoutInflater mLayoutInflater;


    public TimeLineViewAdapter(Context context, ArrayList<TimeLineModel> timeLineModels){
        this.context = context;
        this.timeLineModels = timeLineModels;
    }

    @NonNull
    @Override
    public TimelineViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(parent.getContext());
        }
        View view = mLayoutInflater.inflate(R.layout.item_timeline, parent, false);
        return new TimelineViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TimelineViewHolder holder, int position) {
        TimeLineModel timeLineModel = timeLineModels.get(position);

        Context context = holder.itemView.getContext();

        holder.date.setText(timeLineModel.getHour());
        //holder.timeline.setMarker(ContextCompat.getDrawable(context, R.drawable.icon_edit));
    }

    @Override
    public int getItemCount() {
        return (null != timeLineModels ? timeLineModels.size() : 0);
    }

//    @Override
//    public int getItemViewType(int position) {
//        return TimelineView.getTimeLineViewType(position, getItemCount());
//    }

    public class TimelineViewHolder extends RecyclerView.ViewHolder {

        public TextView date;
        public ImageView image;
        public TimelineView timeline;

        public TimelineViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date = (TextView) itemView.findViewById(R.id.timeline_date);
            this.image = (ImageView) itemView.findViewById(R.id.timeline_image);
            this.timeline = (TimelineView) itemView.findViewById(R.id.timeline);

            // TimelineView 속성 설정
            timeline.setMarker(ContextCompat.getDrawable(context, R.drawable.red_ellipse));
            timeline.setMarkerSize(50); // markerSize 변경
            timeline.setLineWidth(8); // lineWidth 변경
            timeline.setStartLineColor(ContextCompat.getColor(itemView.getContext(), R.color.Bittersweet), 0); // startLineColor 변경
            timeline.setEndLineColor(ContextCompat.getColor(itemView.getContext(), R.color.Bittersweet), 0); // endLineColor 변경
            timeline.setLineStyle(0);
            // viewType 0 : 위아래 1: 아래 2: 위
        }

//        public TimelineViewHolder(@NonNull View itemView, int viewType) {
//            super(itemView);
//            mTimelineView = itemView.findViewById(R.id.timeline);
//            mTimelineView.initLine(viewType);
//        }
    }

}
