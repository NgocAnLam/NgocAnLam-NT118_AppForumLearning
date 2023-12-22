package com.example.forumlearning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MyCommentAdapter extends RecyclerView.Adapter<MyCommentAdapter.CommentViewHolder> {

    private List<Comment> mListComments;
    private IClickListener mIClickListener;
    private IClickUpdateListener mIClickUpdateListener;
    private IClickDeleteListener mIClickDeleteListener;

    public interface IClickListener {
        void onClickItem(Comment comment);
    }

    public interface IClickUpdateListener {
        void onClickUpdateItem(Comment comment);
    }

    public interface IClickDeleteListener {
        void onClickDeleteItem(Comment comment);
    }

    public MyCommentAdapter(List<Comment> mListQuestion, IClickListener listener, IClickUpdateListener listenerUpdate, IClickDeleteListener listenerDelete) {
        this.mListComments = mListQuestion;
        this.mIClickListener = listener;
        this.mIClickUpdateListener = listenerUpdate;
        this.mIClickDeleteListener = listenerDelete;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mListComments.get(position);
        if (comment == null) return;

        Date date = new Date(comment.time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/2021  -  hh:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);

        holder.tvTime.setText(formattedDate);
        holder.tvContent.setText(comment.content);

        holder.itemComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickItem(comment);
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickUpdateListener.onClickUpdateItem(comment);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickDeleteListener.onClickDeleteItem(comment);
            }
        });

    }

    @Override
    public int getItemCount() {
        if (mListComments != null) return mListComments.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTime, tvContent;
        private LinearLayout itemComment;
        private Button btnUpdate, btnDelete;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_my_comments);
            tvContent = itemView.findViewById(R.id.tv_content_my_comments);
            itemComment = itemView.findViewById(R.id.item_my_comment);
            btnUpdate = itemView.findViewById(R.id.btn_update_my_comment);
            btnDelete = itemView.findViewById(R.id.btn_delete_my_comment);
        }
    }
}
