package com.example.mycareer.presenter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.annotation.NonNull;

import com.example.mycareer.R;
import com.example.mycareer.admin.view.fragment.ProfileFragment;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.HomePageView;
import com.example.mycareer.view.activity.AppInfoActivity;
import com.example.mycareer.view.activity.HomePageActivity;
import com.example.mycareer.view.activity.LoginActivity;
import com.example.mycareer.view.activity.SettingsActivity;
import com.example.mycareer.view.fragment.CoursesFragment;
import com.example.mycareer.view.fragment.HomePageFragment;
import com.example.mycareer.view.fragment.PredictionFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class HomePagePresenterImpl implements HomePagePresenter {
    private static final String TAG = HomePagePresenterImpl.class.getSimpleName();
    private static final int PROFILE_ID = 5;
    private HomePageView homePageView;

    private FirebaseAuth auth ;
    private FirebaseAuth.AuthStateListener authListener ;
    private FirebaseDatabase mFirebaseInstance;
    private GoogleSignInClient googleSignInClient;

    private Activity context;
    private int clickedNavItem = 0;

    public HomePagePresenterImpl(FirebaseAuth auth, final Activity context) {
        this.auth = auth;
        this.context = context;
        this.mFirebaseInstance = FirebaseDatabase.getInstance();

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser() ;

                if (user == null) {
                    Utils.showMessage(context,"Bye Bye!!");
                    Utils.setIntent(context, LoginActivity.class);
                    context.finish();
                }
            }
        };
    }

    @Override
    public void attachView(HomePageActivity view) {
        homePageView = view ;
    }

    @Override
    public void detachView() {
        homePageView = null ;
    }

    @Override
    public void initGoogleSetting() {
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                //.requestIdToken("823302886737-agb2190s60dgnt2s78tskojatj157q14.apps.googleusercontent.com")
                .requestIdToken(homePageView.getContext().getResources().getString(R.string.google_web_client_id))
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(homePageView.getContext(), gso);
        homePageView.setGoogleClient(googleSignInClient);
    }

    @Override
    public void logOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        googleSignInClient.signOut()
            .addOnCompleteListener((Activity) homePageView.getContext(), new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Utils.showMessage(context,"Bye Bye!!");
                    Utils.setIntent(context, LoginActivity.class);
                    Profile.getInstance().reset();
                    context.finish();
                }
            });
    }

    @Override
    public void getCurrentUser() {
        homePageView.setUser(auth.getCurrentUser());
    }

    @Override
    public void onStart() {
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        auth.removeAuthStateListener(authListener);
    }

    @Override
    public void handleCloseApp() {
        AlertDialog.Builder builder = new AlertDialog.Builder(homePageView.getContext());
        builder.setMessage(R.string.exit_dialog)
                .setPositiveButton(R.string.pos_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        homePageView.closeApp();
                    }
                })
                .setNegativeButton(R.string.neg_answer, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        builder.show();
    }

    @Override
    public void onSelectedItem(int itemId) {
        clickedNavItem = itemId;
        homePageView.closeDrawer();
    }

    @Override
    public void onDrawerClose() {
        switch (clickedNavItem) {
            case R.id.nav_homepage:
                homePageView.startFragmentWithMessage("Homepage", new HomePageFragment());
                break;
            case R.id.nav_courses:
                homePageView.startFragmentWithMessage("Courses", new CoursesFragment());
                break;
            case R.id.nav_predictions:
                homePageView.startFragmentWithMessage("Predictions", new PredictionFragment());
                break;
            case R.id.nav_info:
                homePageView.startClassWithMessage("App Info", AppInfoActivity.class);
                break;
            case R.id.nav_settings:
                homePageView.startClassWithMessage("Settings", SettingsActivity.class);
                break;
            case R.id.nav_log_out:
                logOut();
                break;
            case R.id.nav_profile:
                homePageView.startFragmentWithMessage("Profiles", new ProfileFragment());
                break;
        }
    }

    @Override
    public void onDrawerOpened() {
        if(Profile.getInstance().isAdmin())
            homePageView.addProfileMenu(PROFILE_ID);
    }
}
