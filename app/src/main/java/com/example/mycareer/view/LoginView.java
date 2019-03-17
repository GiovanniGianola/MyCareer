package com.example.mycareer.view;

import com.example.mycareer.base.BaseView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;

public interface LoginView extends BaseView {
    void isLogin(boolean isLogin, Class cls);
    void setProgressVisibility(boolean visibility);

    void loginGoogleSuccess(GoogleSignInAccount account);
    void loginGoogleError(String error);

    void loginFirebaseSuccess(Class cls);
    void loginFirebaseError(String error);

    void setGoogleClient(GoogleSignInClient googleClient);
}
