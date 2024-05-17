package com.example.pet;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.BoardItemViewHolder> {

    private List<BoardItem> boardItemList;
    private Context context;

    public BoardListAdapter(List<BoardItem> boardItemList, Context context) {
        this.boardItemList = boardItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public BoardItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.board_list_item, parent, false);
        return new BoardItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BoardItemViewHolder holder, int position) {
        BoardItem item = boardItemList.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.contentTextView.setText(item.getContent());
        holder.IDTextView.setText(item.getID());
        holder.DateTextView.setText(item.getDate());

        // 이미지 로드
        if (item.getImageUrl() != null && !item.getImageUrl().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImageUrl())
                    .into(holder.imageView);
        } else {
            //holder.imageView.setImageResource(R.drawable.icon_camera); // 기본 이미지 설정
        }

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BoardDetail.class);
                intent.putExtra("title", item.getTitle());
                intent.putExtra("content",item.getContent());
                intent.putExtra("date",item.getDate());
                intent.putExtra("userID",item.getID());
                intent.putExtra("imageUrl",item.getImageUrl());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardItemList.size();
    }

    public static class BoardItemViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, contentTextView, IDTextView, DateTextView;
        public ImageView imageView;

        public BoardItemViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.itemTitleTextView);
            //contentTextView = view.findViewById(R.id.itemContentTextView);
            imageView = view.findViewById(R.id.itemImageView);

            IDTextView = view.findViewById(R.id.itemUserIDTextView);
            DateTextView = view.findViewById(R.id.itemDateTextView);
        }
    }

}
