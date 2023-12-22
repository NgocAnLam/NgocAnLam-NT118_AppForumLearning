package com.example.forumlearning;

import static android.content.ContentValues.TAG;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class CreateQuestionActivity extends AppCompatActivity {

    private EditText edtTitle, edtContent;
    private Button btnCreateQuestion;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        progressDialog = new ProgressDialog(this);

        initUi();
        initListener();
    }

    private void initUi() {
        edtTitle = findViewById(R.id.edt_title);
        edtContent = findViewById(R.id.edt_content);
        btnCreateQuestion = findViewById(R.id.btn_create);
    }

    private void initListener() {
        btnCreateQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickCreateQuestion();
            }
        });
    }

    private void onClickCreateQuestion() {
        String title = edtTitle.getText().toString();
        String content = edtContent.getText().toString();
        if (title.equals("")) {
            Toast.makeText(CreateQuestionActivity.this, "Title không được trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (content.equals("")) {
            Toast.makeText(CreateQuestionActivity.this, "Content không được trống!", Toast.LENGTH_SHORT).show();
            return;
        }
        progressDialog.show();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        User _user = new User(user.getUid(), user.getEmail(), user.getDisplayName());
        String idQuestion = UUID.randomUUID().toString();
        Date currentTime = Calendar.getInstance().getTime();
        Question question = new Question(idQuestion, _user, title, content, currentTime.getTime());
        myRef.child("questions").child(idQuestion).setValue(question, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@androidx.annotation.Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                progressDialog.dismiss();
                Toast.makeText(CreateQuestionActivity.this, "Question Posted", Toast.LENGTH_SHORT).show();
                finish();
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