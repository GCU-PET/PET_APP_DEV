package com.example.pet;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        holder.IDTextView.setText(item.getWriter());
        holder.DateTextView.setText(item.getDate());

        // 이미지 로드
        if (item.getImage() != null && !item.getImage().isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(item.getImage())
                    .into(holder.imageView);
            Log.e("image", "이미지로드 성공");
        } else {
            Log.e("image", "이미지 URL이 null이거나 비어 있습니다.");
            // 이미지 URL이 비어있거나 null일 때
            Glide.with(holder.itemView.getContext())
                    .clear(holder.imageView); // 이미지 뷰를 비웁니다.
        }

        // 아이템 클릭 리스너 설정
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, BoardDetail.class);
                intent.putExtra("_id", item.get_id());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("content",item.getContent());
                intent.putExtra("date",item.getDate());
                intent.putExtra("writer",item.getWriter());
                intent.putExtra("imageUrl",item.getImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return boardItemList.size();
    }

    public static class BoardItemViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, IDTextView, DateTextView;
        public ImageView imageView;

        public BoardItemViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.itemTitleTextView);
            imageView = view.findViewById(R.id.itemImageView);

            IDTextView = view.findViewById(R.id.itemUserIDTextView);
            DateTextView = view.findViewById(R.id.itemDateTextView);
        }
    }

}
