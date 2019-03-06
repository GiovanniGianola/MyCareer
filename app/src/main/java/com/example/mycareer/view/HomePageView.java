package com.example.mycareer.view;

import com.example.mycareer.base.BaseView;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.firebase.auth.FirebaseUser;

public interface HomePageView extends BaseView {
    void setUser(FirebaseUser user);
    void setGoogleClient(GoogleSignInClient googleClient);

    void databaseError(String error);

    void closeApp();
}
