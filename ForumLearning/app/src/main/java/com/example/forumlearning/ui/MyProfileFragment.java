package com.example.forumlearning.ui;

import static com.example.forumlearning.MainActivity.MY_REQUEST_CODE;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.forumlearning.MainActivity;
import com.example.forumlearning.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import javax.annotation.Nullable;

public class MyProfileFragment extends Fragment {

    private View mView;
    private ImageView imgAvatar;
    private EditText edtFullName, edtEmail;
    private Button btnUpdateProfile;
    private Uri mUri;
    private MainActivity mainActivity;
    private ProgressDialog progressDialog;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_profile, container, false);

        initUi();
        mainActivity = (MainActivity) getActivity();
        progressDialog = new ProgressDialog(getActivity());
        setUserInformation();
        initListener();

        return mView;
    }

    private void initUi() {
        imgAvatar = mView.findViewById(R.id.img_avatar);
        edtFullName = mView.findViewById(R.id.edt_fullname);
        edtEmail = mView.findViewById(R.id.edt_email);
        btnUpdateProfile = mView.findViewById(R.id.btn_update_profile);
    }

    private void setUserInformation() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        edtFullName.setText(user.getDisplayName());
        edtEmail.setText(user.getEmail());
        Glide.with(getActivity()).load(user.getPhotoUrl()).error(R.drawable.ic_avatar_default).into(imgAvatar);
    }

    private void initListener() {
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRequestPermission();
            }

        });

        btnUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickUpdateProfile();
            }
        });
    }

    private void onClickUpdateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) return;
        String strFullName = edtFullName.getText().toString().trim();
        progressDialog.show();
        if (user.getEmail() != edtEmail.getText().toString().trim()) {
            user.updateEmail(edtEmail.getText().toString().trim())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                mainActivity.showUserInformation();
                            }
                        }
                    });
        }
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(strFullName)
                .setPhotoUri(mUri)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Cập nhập Profile thành công!", Toast.LENGTH_SHORT).show();
                            mainActivity.showUserInformation();
                        }
                    }
                });
    }

    private void onClickRequestPermission() {
        if (mainActivity == null) {
            return;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            mainActivity.openGllery();
            return;
        }

        if (getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mainActivity.openGllery();
        } else {
            String [] permissions = {Manifest.permission.READ_EXTERNAL_STORAGE};
            getActivity().requestPermissions(permissions, MY_REQUEST_CODE);
        }
    }

    public void setBitmapImageView(Bitmap bitmapImageView) {
        imgAvatar.setImageBitmap(bitmapImageView);
    }
    
    public void setUri(Uri mUri) {
        this.mUri = mUri;
    }
}