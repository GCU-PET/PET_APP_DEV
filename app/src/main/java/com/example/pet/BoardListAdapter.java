package com.example.pet;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BoardListAdapter extends RecyclerView.Adapter<BoardListAdapter.BoardItemViewHolder> {

    private List<BoardItem> boardItemList;

    public BoardListAdapter(List<BoardItem> boardItemList) {
        this.boardItemList = boardItemList;
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

        holder.imageView.setImageURI(item.getImageUri());
        holder.titleTextView.setText(item.getTitle());
        holder.contentTextView.setText(item.getContent());
        holder.IDTextView.setText(item.getContent());
        holder.DateTextView.setText(item.getContent());
    }

    @Override
    public int getItemCount() {
        return boardItemList.size();
    }

    public class BoardItemViewHolder extends RecyclerView.ViewHolder {
        public TextView titleTextView, contentTextView, IDTextView, DateTextView;
        public ImageView imageView;

        public BoardItemViewHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.itemTitleTextView);
            contentTextView = view.findViewById(R.id.itemContentTextView);
            imageView = view.findViewById(R.id.itemImageView);

            IDTextView = view.findViewById(R.id.itemUserIDTextView);
            DateTextView = view.findViewById(R.id.itemDateTextView);
        }
    }

}
