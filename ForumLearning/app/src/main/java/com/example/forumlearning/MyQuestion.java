package com.example.forumlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.List;

public class MyQuestion extends AppCompatActivity {

    private RecyclerView rcQuestions;
    private MyQuestionAdapter mMyQuestionAdapter;
    private List<Question> mListQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_question);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rcQuestions = findViewById(R.id.rcv_my_questions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcQuestions.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcQuestions.addItemDecoration(dividerItemDecoration);

        mListQuestions = new ArrayList<>();
        mMyQuestionAdapter = new MyQuestionAdapter(mListQuestions, new MyQuestionAdapter.IClickListener() {
            @Override
            public void onClickItem(Question question) {
                Intent intent = new Intent(MyQuestion.this, DetailQuestion.class);
                intent.putExtra("questionID", question.getId());
                startActivity(intent);
            }
        }, new MyQuestionAdapter.IClickUpdateListener() {
            @Override
            public void onClickUpdateItem(Question question) {
                openDialogUpdateItem(question);
            }
        }, new MyQuestionAdapter.IClickDeleteListener() {
            @Override
            public void onClickDeleteItem(Question question) {
                onClickDeleteData(question);
            }
        });

        rcQuestions.setAdapter(mMyQuestionAdapter);

        getListQuestionFromRealtimeDatabase();

    }

    private void onClickDeleteData(Question question) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xoá câu hỏi này?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("questions");

                        myRef.child(String.valueOf(question.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MyQuestion.this, "Delete question success!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openDialogUpdateItem(Question question) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_upadte_question);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtTitle = dialog.findViewById(R.id.edt_update_title_question),
                edtContent= dialog.findViewById(R.id.edt_update_content_question);
        Button btnCancel  = dialog.findViewById(R.id.btn_cancel_update_question) ,
                btnUpdate = dialog.findViewById(R.id.btn_update_question);

        edtTitle.setText(question.getTitle());
        edtContent.setText(question.getContent());

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("questions");

                String newTitle = edtTitle.getText().toString().trim();
                String newContent = edtContent.getText().toString().trim();
                question.setTitle(newTitle);
                question.setContent(newContent);

                myRef.child(String.valueOf(question.getId())).updateChildren(question.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(MyQuestion.this, "Update question success!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void getListQuestionFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("questions");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query = myRef.orderByChild("author/id").equalTo(user.getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Question question = snapshot.getValue(Question.class);
                if (question != null) {
                    mListQuestions.add(0, question);
                    mMyQuestionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Question question =  snapshot.getValue(Question.class);
                if (mListQuestions == null || mListQuestions.isEmpty()) {
                    return;
                }
                for (int i=0; i< mListQuestions.size(); i++) {
                    if (question.getId().equals(mListQuestions.get(i).getId())) {
                        mListQuestions.set(i, question);
                        break;
                    }
                }
                mMyQuestionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Question question =  snapshot.getValue(Question.class);
                if (mListQuestions == null || mListQuestions.isEmpty()) {
                    return;
                }
                for (int i=0; i< mListQuestions.size(); i++) {
                    if (question.getId().equals(mListQuestions.get(i).getId())) {
                        mListQuestions.remove(mListQuestions.get(i));
                        break;
                    }
                }
                mMyQuestionAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}