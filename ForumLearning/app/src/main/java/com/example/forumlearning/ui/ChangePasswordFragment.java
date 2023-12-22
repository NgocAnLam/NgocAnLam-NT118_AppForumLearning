package com.example.forumlearning.ui;

import static com.example.forumlearning.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.forumlearning.MainActivity;
import com.example.forumlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.annotation.Nullable;

public class ChangePasswordFragment extends Fragment {

    private View mView;
    private EditText edtNewPassword, edtNewPasswordConfirm;
    private Button btnChangePassword;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_change_password, container, false);

        initUi();
        progressDialog = new ProgressDialog(getActivity());
        initListener();
        return mView;
    }

    private void initUi() {
        edtNewPassword = mView.findViewById(R.id.edt_new_password);
        edtNewPasswordConfirm = mView.findViewById(R.id.edt_new_password_confirm);
        btnChangePassword = mView.findViewById(R.id.btn_change_password);
    }

    private void initListener() {
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickChangePassword();
            }
        });
    }

    private void onClickChangePassword() {
        String strNewPassword = edtNewPassword.getText().toString().trim();
        String strNewPasswordConfirm = edtNewPasswordConfirm.getText().toString().trim();

        if (!strNewPassword.equals(strNewPasswordConfirm)) {
            Toast.makeText(getActivity(), "Wrong Password Confirm!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.updatePassword(strNewPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "User password updated!", Toast.LENGTH_SHORT).show();
                        } else {
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            builder.setTitle("Password Current:");
                            final EditText input = new EditText(getActivity());
                            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            builder.setView(input);

                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    reAuthenticate(input.getText().toString());
                                }
                            });
                            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });

                            builder.show();
                        }
                    }
                });
    }
    
    private void reAuthenticate(String password) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        // Get auth credentials from the user for re-authentication. The example below shows
        // email and password credentials but there are multiple possible providers,
        // such as GoogleAuthProvider or FacebookAuthProvider.
        AuthCredential credential = EmailAuthProvider
                .getCredential(user.getEmail(), password);

        // Prompt the user to re-provide their sign-in credentials
        user.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            onClickChangePassword();
                        } else {
                            Toast.makeText(getActivity(), "Wrong password!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}