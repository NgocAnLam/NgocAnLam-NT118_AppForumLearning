package com.example.forumlearning.ui.signup;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.forumlearning.Login;
import com.example.forumlearning.ProfileUpdate;
import com.example.forumlearning.R;
import com.example.forumlearning.databinding.FragmentHomeBinding;
import com.example.forumlearning.databinding.FragmentProfileBinding;
import com.example.forumlearning.databinding.FragmentSignupBinding;
import com.example.forumlearning.ui.home.HomeViewModel;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;

    public static SignupFragment newInstance() {
        return new SignupFragment();
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_signup, container, false);

        final TextView textLogin = view.findViewById(R.id.text_login);
        textLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),
//                        "The favorite list would appear on clicking this icon",
//                        Toast.LENGTH_LONG).show();
                Intent loginScreen = new Intent(getActivity(), Login.class);
                startActivity(loginScreen);
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}