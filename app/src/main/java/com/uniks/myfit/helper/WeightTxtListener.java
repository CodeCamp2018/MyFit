package com.uniks.myfit.helper;

import android.text.Editable;
import android.text.TextWatcher;

import com.uniks.myfit.database.AppDatabase;
import com.uniks.myfit.database.User;

public class WeightTxtListener implements TextWatcher {

    private AppDatabase db;
    private User user;

    CharSequence changedTxt = "";

    public WeightTxtListener(AppDatabase db, User user) {
        this.db = db;
        this.user = user;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        changedTxt = s;
    }

    @Override
    public void afterTextChanged(Editable s) {
        System.out.println("textChange to " + changedTxt);
        // user is done change the weight -> save the new weight
        if (changedTxt.length() != 0) {
            user.setWeight(Integer.parseInt(changedTxt.toString()));
            db.userDao().updateUser(user);
        } else {

        }
    }
}
