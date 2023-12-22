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

public class MyQuestionAdapter extends RecyclerView.Adapter<MyQuestionAdapter.QuestionViewHolder> {

    private List<Question> mListQuestion;
    private IClickListener mIClickListener;
    private IClickUpdateListener mIClickUpdateListener;
    private IClickDeleteListener mIClickDeleteListener;

    public interface IClickListener {
        void onClickItem(Question question);
    }

    public interface IClickUpdateListener {
        void onClickUpdateItem(Question question);
    }

    public interface IClickDeleteListener {
        void onClickDeleteItem(Question question);
    }

    public MyQuestionAdapter(List<Question> mListQuestion, IClickListener listener, IClickUpdateListener listenerUpdate, IClickDeleteListener listenerDelete) {
        this.mListQuestion = mListQuestion;
        this.mIClickListener = listener;
        this.mIClickUpdateListener = listenerUpdate;
        this.mIClickDeleteListener = listenerDelete;
    }

    @NonNull
    @Override
    public QuestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_question, parent, false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionViewHolder holder, int position) {
        Question question = mListQuestion.get(position);
        if (question == null) return;
        User user = question.getAuthor();
        holder.tvTitleQuestion.setText(question.title);

        Date date = new Date(question.time*1000L); // *1000 is to convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/2021  -  hh:mm:ss"); // the format of your date
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7")); // give a timezone reference for formating (see comment at the bottom
        String formattedDate = sdf.format(date);
        holder.tvTimeQuestion.setText(formattedDate);

        holder.itemQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickListener.onClickItem(question);
            }
        });

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickUpdateListener.onClickUpdateItem(question);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIClickDeleteListener.onClickDeleteItem(question);
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mListQuestion != null) return mListQuestion.size();
        return 0;
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder {

        private TextView tvTitleQuestion, tvTimeQuestion;
        private LinearLayout itemQuestion;
        private Button btnDelete, btnUpdate;

        public QuestionViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitleQuestion = itemView.findViewById(R.id.tv_title_my_question);
            tvTimeQuestion = itemView.findViewById(R.id.tv_time_my_question);
            itemQuestion = itemView.findViewById(R.id.item_my_question);
            btnUpdate = itemView.findViewById(R.id.btn_update_my_question);
            btnDelete = itemView.findViewById(R.id.btn_delete_my_question);
        }
    }
}
