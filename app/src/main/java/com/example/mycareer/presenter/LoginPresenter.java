package com.example.mycareer.presenter;

import com.example.mycareer.base.BasePresenter;
import com.example.mycareer.view.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.Task;

public interface LoginPresenter extends BasePresenter<LoginView> {
    void checkLogin();

    void initGoogleSetting();

    void firebaseAuthWithGoogle(GoogleSignInAccount acct);
    void handleGoogleSignInResult(Task<GoogleSignInAccount> task);
}
