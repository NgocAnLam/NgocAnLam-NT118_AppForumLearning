package com.example.forumlearning.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public ProfileViewModel() {
//        mText = new MutableLiveData<>();
//        mText.setValue("This is profile fragmenta");
    }

    public LiveData<String> getText() {
        return mText;
    }
}