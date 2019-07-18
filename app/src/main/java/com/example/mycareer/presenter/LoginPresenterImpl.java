package com.example.mycareer.presenter;

import android.app.Activity;
import androidx.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.example.mycareer.R;
import com.example.mycareer.model.Course;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Constants;
import com.example.mycareer.utils.MyApplication;
import com.example.mycareer.utils.SharedPrefManager;
import com.example.mycareer.view.LoginView;
import com.example.mycareer.view.activity.HomePageActivity;
import com.example.mycareer.view.activity.SettingsActivity;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginPresenterImpl implements LoginPresenter {
    private static final String TAG = LoginPresenterImpl.class.getSimpleName();
    private LoginView loginView ;
    private FirebaseAuth auth ;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;
    private GoogleSignInClient googleSignInClient;

    public LoginPresenterImpl(FirebaseAuth auth) {
        this.auth = auth;
        mFirebaseInstance = FirebaseDatabase.getInstance();
    }

    @Override
    public void attachView(LoginView view) {
        loginView = view ;
    }

    @Override
    public void detachView() {
        loginView = null ;
    }

    @Override
    public void checkLogin() {
        if (auth.getCurrentUser() != null) {
            FirebaseUser user = auth.getCurrentUser();
            createUpdateUser(user);

            loginView.isLogin(true, checkSettings());
        }
        else
            loginView.isLogin(false, null);
    }

    private Class checkSettings(){
        if(SharedPrefManager.getIntPrefVal(MyApplication.getAppContext(), Constants.Strings.SPREF_TOT_CREDITS_KEY, -1) == -1)
            return SettingsActivity.class;
        else
            return HomePageActivity.class;
    }

    @Override
    public void initGoogleSetting() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("823302886737-agb2190s60dgnt2s78tskojatj157q14.apps.googleusercontent.com")
                .requestIdToken(loginView.getContext().getResources().getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(loginView.getContext(), gso);
        loginView.setGoogleClient(googleSignInClient);
    }

    @Override
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        auth.signInWithCredential(credential).addOnCompleteListener((Activity) loginView.getContext(), new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        FirebaseUser user = auth.getCurrentUser();
                        createUpdateUser(user);

                        loginView.setProgressVisibility(true);
                        loginView.loginFirebaseSuccess(checkSettings());
                    } else {
                        // If sign in fails, display a message to the user.
                        loginView.setProgressVisibility(false);
                        loginView.loginFirebaseError(task.getException().getMessage());
                    }
                }
            });
    }

    @Override
    public void handleGoogleSignInResult(Task<GoogleSignInAccount> task) {
        try {
            // Google Sign In was successful, authenticate with Firebase
            GoogleSignInAccount account = task.getResult(ApiException.class);
            loginView.setProgressVisibility(true);
            loginView.loginGoogleSuccess(account);
        } catch (ApiException e) {
            // Google Sign In failed, update UI appropriately
            loginView.setProgressVisibility(false);
            loginView.loginGoogleError(e.getMessage());
        }
    }

    private void createUpdateUser(FirebaseUser currentUser) {
        Profile user = Profile.getInstance();
        user.setUserId(currentUser.getUid());
        user.setName(currentUser.getDisplayName());
        user.setEmail(currentUser.getEmail());
        user.setPhotoUri(currentUser.getPhotoUrl());
        //user.setAdmin(false);

        this.mFirebaseInstance = FirebaseDatabase.getInstance();
        // get reference to 'users' node
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("users");
        // updating the user via child nodes
        if (!TextUtils.isEmpty(user.getName()))
            this.mFirebaseDatabase.child(user.getUserId()).child("name").setValue(user.getName());

        if (!TextUtils.isEmpty(user.getEmail()))
            this.mFirebaseDatabase.child(user.getUserId()).child("email").setValue(user.getEmail());

        this.mFirebaseDatabase.child(user.getUserId()).child("profilePic").setValue(user.getPhotoUri().toString());

        downloadCourses(currentUser);
        setAdmin(currentUser);
    }

    private void setAdmin(FirebaseUser user){
        mFirebaseInstance.getReference("users/" + user.getUid()).child("isAdmin").addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue() != null) {
                    Profile.getInstance().setAdmin((boolean) dataSnapshot.getValue());
                }
                else
                    mFirebaseDatabase.child(user.getUid()).child("isAdmin").setValue(false);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w(TAG, "Firebase Database Error: " + databaseError.getMessage());
            }
        });
    }

    private void downloadCourses(FirebaseUser user){
        mFirebaseInstance.getReference("users/" + user.getUid()).child("courses").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Course> courseList= new ArrayList<>();

                for(DataSnapshot data: dataSnapshot.getChildren()){
                    Course c = data.getValue(Course.class);
                    courseList.add(c);
                }
                Profile.getInstance().setCourseList(courseList);
                Log.d(TAG, "Data Retrived: " + courseList);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w(TAG, "Firebase Database Error: " + databaseError.getMessage());
            }
        });
    }
}
