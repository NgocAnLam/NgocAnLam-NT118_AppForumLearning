package com.example.forumlearning;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.forumlearning.ui.home.HomeFragment;

import java.util.List;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.QuestionViewHolder> {

    private List<Question> mListQuestion;
    private IClickListener mIClickListener;

    public interface IClickListener {
        void onClickItem(Question question);
    }


    public QuestionAdapter(List<Question> mListQuestion, IClickListener listener) {
        this.mListQuestion = mListQuestion;
        this.mIClickListener = listener;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = mListQuestion.get(position);
        if (question == null) return;
        User user = question.getAuthor();
        holder.tvUserName.setText("Author: " + user.getFullname());
        holder.tvTitleQuestion.setText(question.title);

        holder.itemQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickItem(question);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(mListQuestion != null) return mListQuestion.size();
        return 0;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView tvUserName, tvTitleQuestion;
        private LinearLayout itemQuestion;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tv_username);
            tvTitleQuestion = itemView.findViewById(R.id.tv_title_question);
            itemQuestion = itemView.findViewById(R.id.item_question);
        }
    }
}
