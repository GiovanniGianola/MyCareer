package com.example.mycareer.view.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseActivity;
import com.example.mycareer.presenter.LoginPresenter;
import com.example.mycareer.presenter.LoginPresenterImpl;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.LoginView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity  extends BaseActivity implements LoginView, View.OnClickListener {
    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final int RC_SIGN_IN =  3;

    private ProgressBar progressBar;
    private SignInButton signInButton;
    private View mView;

    private FirebaseAuth mAuth;
    private GoogleSignInOptions gso;

    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInAccount account;

    private LoginPresenter loginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mView = findViewById(R.id.activity_layout);

        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(this);
        progressBar = findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        loginPresenter = new LoginPresenterImpl(mAuth);
        loginPresenter.attachView(this);
        loginPresenter.checkLogin();
        loginPresenter.initGoogleSetting();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void isLogin(boolean isLogin) {
        Log.d(TAG, "IsLogin: " + isLogin);
        if (isLogin) {
            Utils.showMessage(this, "Welcome back " + mAuth.getCurrentUser().getDisplayName() + "!");
            Utils.setIntent(this, HomePageActivity.class);
            finish();
        }
    }

    @Override
    public void setProgressVisibility(boolean visibility) {
        if (visibility)
            progressBar.setVisibility(View.VISIBLE);
        else
            progressBar.setVisibility(View.GONE);
    }

    @Override
    public void loginGoogleSuccess(GoogleSignInAccount account) {
        this.account = account;
        Utils.showMessage(this, "Login Google Success !");
        loginPresenter.firebaseAuthWithGoogle(account);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loginGoogleError(String error) {
        Utils.showSnackbar(mView, "Login Google Error ! " + error);
    }

    @SuppressLint("ResourceType")
    @Override
    public void loginFirebaseSuccess() {
        Utils.showMessage(this, "Welcome " + account.getDisplayName() + "!");
        Utils.setIntent(this, HomePageActivity.class);
        finish();
    }

    @SuppressLint("ResourceType")
    @Override
    public void loginFirebaseError(String error) {
        Utils.showSnackbar(mView, "Login Firebase Error ! " + error);
    }

    @Override
    public void setGoogleClient(GoogleSignInClient googleClient) {
        mGoogleSignInClient = googleClient;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        loginPresenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            loginPresenter.handleGoogleSignInResult(task);
        }
    }

    @Override
    public void onClick(View v) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
}
