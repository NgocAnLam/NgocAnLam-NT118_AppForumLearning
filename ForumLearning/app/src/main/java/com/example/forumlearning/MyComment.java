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

public class MyComment extends AppCompatActivity {

    private RecyclerView rcComments;
    private MyCommentAdapter mMyCommentAdapter;
    private List<Comment> mListComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_comment);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        rcComments = findViewById(R.id.rcv_my_comments);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcComments.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcComments.addItemDecoration(dividerItemDecoration);

        mListComments = new ArrayList<>();
        mMyCommentAdapter = new MyCommentAdapter(mListComments, new MyCommentAdapter.IClickListener() {
            @Override
            public void onClickItem(Comment comment) {
                Intent intent = new Intent(MyComment.this, DetailQuestion.class);
                intent.putExtra("questionID", comment.getIdQuestion());
                startActivity(intent);
            }
        }, new MyCommentAdapter.IClickUpdateListener() {
            @Override
            public void onClickUpdateItem(Comment comment) {
                openDialogUpdateItem(comment);
            }
        }, new MyCommentAdapter.IClickDeleteListener() {
            @Override
            public void onClickDeleteItem(Comment comment) {
                onClickDeleteData(comment);
            }
        });
        rcComments.setAdapter(mMyCommentAdapter);

        getListCommentsFromRealtimeDatabase();

    }

    private void onClickDeleteData(Comment comment) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc chắn muốn xoá comment này?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("comments");

                        myRef.child(String.valueOf(comment.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(MyComment.this, "Delete comment success!", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void openDialogUpdateItem(Comment comment) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_dialog_upadte_comment);
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);

        EditText edtContent= dialog.findViewById(R.id.edt_update_content_comment);
        Button btnCancel  = dialog.findViewById(R.id.btn_cancel_update_comment) ,
                btnUpdate = dialog.findViewById(R.id.btn_update_comment);

        edtContent.setText(comment.getContent());

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
                DatabaseReference myRef = database.getReference("comments");

                String newContent = edtContent.getText().toString().trim();
                comment.setContent(newContent);

                myRef.child(String.valueOf(comment.getId())).updateChildren(comment.toMap(), new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(MyComment.this, "Update comment success!", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });

        dialog.show();
    }

    private void getListCommentsFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("comments");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Query query = myRef.orderByChild("author/id").equalTo(user.getUid());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                if (comment != null) {
                    mListComments.add(0, comment);
                    mMyCommentAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment =  snapshot.getValue(Comment.class);
                if (mListComments == null || mListComments.isEmpty()) {
                    return;
                }
                for (int i=0; i< mListComments.size(); i++) {
                    if (comment.getId().equals(mListComments.get(i).getId())) {
                        mListComments.set(i, comment);
                        break;
                    }
                }
                mMyCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Question question =  snapshot.getValue(Question.class);
                if (mListComments == null || mListComments.isEmpty()) {
                    return;
                }
                for (int i=0; i< mListComments.size(); i++) {
                    if (question.getId().equals(mListComments.get(i).getId())) {
                        mListComments.remove(mListComments.get(i));
                        break;
                    }
                }
                mMyCommentAdapter.notifyDataSetChanged();
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