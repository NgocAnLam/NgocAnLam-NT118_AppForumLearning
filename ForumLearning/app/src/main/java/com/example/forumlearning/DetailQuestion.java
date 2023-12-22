package com.example.forumlearning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.UUID;

public class DetailQuestion extends AppCompatActivity {

    private Question question;
    private TextView tvTitle, tvContent, tvAuthor, tvTime;
    private EditText edtComment;
    private ProgressDialog progressDialog;
    private Button btnComment;
    private RecyclerView rcComments;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mListComments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_question);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        initUi();

        Bundle b = getIntent().getExtras();
        String id = b.getString("questionID");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("questions");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Question _question = postSnapshot.getValue(Question.class);
                    if (id.equals(_question.getId())) {
                        question = _question;
                        tvTitle.setText(question.getTitle());
                        tvAuthor.setText(question.author.getFullname());
                        tvContent.setText(question.getContent());

                        Date date = new Date(question.time*1000L);
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/2021");
                        sdf.setTimeZone(TimeZone.getTimeZone("GMT+7"));
                        String formattedDate = sdf.format(date);
                        tvTime.setText(formattedDate);
                        getListCommentFromRealtimeDatabase();
                        return;
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateComment();
            }
        });

    }

    private void initUi() {
        tvTitle = findViewById(R.id.tv_title_question);
        tvAuthor = findViewById(R.id.tv_author_question);
        tvContent = findViewById(R.id.tv_content_question);
        tvTime = findViewById(R.id.tv_time_question);
        edtComment = findViewById(R.id.edt_comment);
        btnComment = findViewById(R.id.btn_comment);
        rcComments = findViewById(R.id.rcv_comments);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rcComments.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rcComments.addItemDecoration(dividerItemDecoration);

        mListComments = new ArrayList<>();
        mCommentAdapter = new CommentAdapter(mListComments);
        rcComments.setAdapter(mCommentAdapter);
    }

    private void getListCommentFromRealtimeDatabase() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("comments");

        Query query = myRef.orderByChild("idQuestion").equalTo(question.getId());
        query.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Comment comment = snapshot.getValue(Comment.class);
                if (question != null) {
                    mListComments.add(comment);
                    mCommentAdapter.notifyDataSetChanged();
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

                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Comment comment =  snapshot.getValue(Comment.class);
                if (mListComments == null || mListComments.isEmpty()) {
                    return;
                }
                for (int i=0; i< mListComments.size(); i++) {
                    if (comment.getId().equals(mListComments.get(i).getId())) {
                        mListComments.remove(mListComments.get(i));
                        break;
                    }
                }

                mCommentAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void onClickCreateComment() {
        String content = edtComment.getText().toString();
        if (content.equals("")) {
            Toast.makeText(this, "Comment không được để trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        User _user = new User(user.getUid(), user.getEmail(), user.getDisplayName());
        String idComment = UUID.randomUUID().toString();
        Date currentTime = Calendar.getInstance().getTime();

        Comment comment = new Comment(idComment, question.getId(), _user, content, currentTime.getTime());
        myRef.child("comments").child(idComment).setValue(comment, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                edtComment.setText("");
                Toast.makeText(DetailQuestion.this, "Comment Posted", Toast.LENGTH_SHORT).show();
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