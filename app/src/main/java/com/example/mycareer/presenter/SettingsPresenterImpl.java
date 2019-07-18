package com.example.mycareer.presenter;

import android.content.Context;

import com.example.mycareer.R;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Constants;
import com.example.mycareer.utils.SharedPrefManager;
import com.example.mycareer.view.SettingsView;
import com.example.mycareer.view.activity.SettingsActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private static final int REQ_DIALOG_TOT_CREDITS = 0;
    private static final int REQ_DIALOG_LAUDE_VAL = 1;
    private SettingsView settingsView;

    private Context context;

    private int totCreditsVal = 0;
    private int laudeValueVal = 0;

    private FirebaseAuth auth ;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference mFirebaseDatabase;

    public SettingsPresenterImpl(final Context context){
        this.context = context;
        mFirebaseInstance = FirebaseDatabase.getInstance();
    }

    @Override
    public void attachView(SettingsActivity view) {
        settingsView = view;
    }

    @Override
    public void detachView() {
        settingsView = null;
    }

    @Override
    public void setOnClickListenerCardViewTotCredits() {
        settingsView.showDialog(context.getResources().getString(R.string.dialog_title_tot_credits), REQ_DIALOG_TOT_CREDITS);
    }

    @Override
    public void setOnClickListenerCardViewLaudeVal() {
        settingsView.showDialog(context.getResources().getString(R.string.dialog_title_laude_val), REQ_DIALOG_LAUDE_VAL);
    }

    @Override
    public void setOnClickListenerTextViewOk(int dialogRequest) {
        switch(dialogRequest){
            case REQ_DIALOG_TOT_CREDITS:
                handleTotCreditsReq();
                break;
            case REQ_DIALOG_LAUDE_VAL:
                handleLaudeValueReq();
                break;
            default:
                settingsView.dismissDialog();
        }
        settingsView.dismissDialog();
    }

    private void handleTotCreditsReq(){
        CharSequence cs = settingsView.getEditTextVal();
        if(cs.toString().isEmpty())
            return;

        totCreditsVal = Integer.parseInt(cs.toString());
        if(totCreditsVal < Profile.getInstance().getStatistics().getTotalCfu()) {
            settingsView.onError(String.format(Locale.getDefault(), "%s %d", "The value must be greater than or equal to", Profile.getInstance().getStatistics().getTotalCfu()));
            return;
        }
        settingsView.setTotCredits(totCreditsVal);
    }

    private void handleLaudeValueReq(){
        CharSequence cs = settingsView.getEditTextVal();
        if(cs.toString().isEmpty())
            return;

        laudeValueVal = Integer.parseInt(cs.toString());
        if(laudeValueVal < 30 || laudeValueVal > 39) {
            settingsView.onError(String.format(Locale.getDefault(), "%s", "The value must be between 30 and 39"));
            return;
        }
        settingsView.setLaudeVal(laudeValueVal);
    }

    @Override
    public void setOnClickListenerTextViewCancel() {
        settingsView.dismissDialog();
    }

    @Override
    public void setOnClickListenerButtonSave() {
        if(laudeValueVal != 0) {
            uploadSettingLAudeValue();
            SharedPrefManager.setIntPrefVal(settingsView.getContext(), Constants.Strings.SPREF_LAUDE_VALUE_KEY, laudeValueVal);
        }
        if(totCreditsVal != 0) {
            uploadSettingTotCredits();
            SharedPrefManager.setIntPrefVal(settingsView.getContext(), Constants.Strings.SPREF_TOT_CREDITS_KEY, totCreditsVal);
        }

        settingsView.refreshApp();
    }



    private void uploadSettingTotCredits(){
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("users").child(Profile.getInstance().getUserId()).child("settings");
        this.mFirebaseDatabase.child("totCredits").setValue(totCreditsVal);

        Profile.getInstance().getSettings().setTotCredits(totCreditsVal);
    }

    private void uploadSettingLAudeValue(){
        this.mFirebaseDatabase = this.mFirebaseInstance.getReference("users").child(Profile.getInstance().getUserId()).child("settings");
        this.mFirebaseDatabase.child("laudeValue").setValue(laudeValueVal);

        Profile.getInstance().getSettings().setLaudeValue(laudeValueVal);
    }
}
