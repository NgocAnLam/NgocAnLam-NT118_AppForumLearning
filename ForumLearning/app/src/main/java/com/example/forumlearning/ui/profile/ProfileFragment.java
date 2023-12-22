package com.example.forumlearning.ui.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.forumlearning.MainActivity;
import com.example.forumlearning.MyComment;
import com.example.forumlearning.MyQuestion;
import com.example.forumlearning.ProfileUpdate;
import com.example.forumlearning.R;
import com.example.forumlearning.databinding.FragmentProfileBinding;

public class ProfileFragment extends Fragment {

    private FragmentProfileBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile, container, false);

        binding = FragmentProfileBinding.inflate(inflater, container, false);
//        View root = binding.getRoot();

        final LinearLayout profileUpdate = view.findViewById(R.id.show_update_profile);
        profileUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getActivity(),
//                        "The favorite list would appear on clicking this icon",
//                        Toast.LENGTH_LONG).show();
                Intent profileUpdate = new Intent(getActivity(), ProfileUpdate.class);
                startActivity(profileUpdate);
            }
        });

        final LinearLayout myQuestion = view.findViewById(R.id.show_my_question);
        myQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileUpdate = new Intent(getActivity(), MyQuestion.class);
                startActivity(profileUpdate);
            }
        });

        final LinearLayout myComment = view.findViewById(R.id.show_my_comment);
        myComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileUpdate = new Intent(getActivity(), MyComment.class);
                startActivity(profileUpdate);
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