package com.example.mycareer.presenter;

import android.app.Activity;
import android.content.Context;

import com.example.mycareer.R;
import com.example.mycareer.model.Profile;
import com.example.mycareer.utils.Constants;
import com.example.mycareer.utils.SharedPrefManager;
import com.example.mycareer.view.SettingsView;
import com.example.mycareer.view.activity.SettingsActivity;

import java.util.Locale;

public class SettingsPresenterImpl implements SettingsPresenter {
    private static final String TAG = SettingsPresenterImpl.class.getSimpleName();
    private static final int REQ_DIALOG_TOT_CREDITS = 0;
    private static final int REQ_DIALOG_LAUDE_VAL = 1;
    private SettingsView settingsView;

    private Context context;

    private int totCreditsVal = 0;
    private int laudeValueVal = 0;

    public SettingsPresenterImpl(final Context context){
        this.context = context;
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

        SharedPrefManager.setIntPrefVal(settingsView.getContext(), Constants.Strings.SPREF_TOT_CREDITS_KEY, totCreditsVal);
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

        SharedPrefManager.setIntPrefVal(settingsView.getContext(), Constants.Strings.SPREF_LAUDE_VALUE_KEY, laudeValueVal);
        settingsView.setLaudeVal(laudeValueVal);
    }

    @Override
    public void setOnClickListenerTextViewCancel() {
        settingsView.dismissDialog();
    }

    @Override
    public void setOnClickListenerButtonSave() {
        System.out.println(laudeValueVal + " - " + totCreditsVal);
        if(laudeValueVal != 0 || totCreditsVal != 0)
            settingsView.refreshApp();
    }
}
