package com.example.forumlearning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<Comment> mListComments;

    public CommentAdapter(List<Comment> mListComments) {
        this.mListComments = mListComments;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = mListComments.get(position);
        if (comment == null) return;
        holder.tvAuthor.setText(comment.author.getFullname());
        holder.tvContent.setText(comment.content);
    }

    @Override
    public int getItemCount() {
        if (mListComments != null) return mListComments.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private TextView tvAuthor, tvContent;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.tv_author_comment);
            tvContent = itemView.findViewById(R.id.tv_content_comment);
        }
    }
}
