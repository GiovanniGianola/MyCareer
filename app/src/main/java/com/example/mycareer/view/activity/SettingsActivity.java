package com.example.mycareer.view.activity;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseActivity;
import com.example.mycareer.model.Profile;
import com.example.mycareer.presenter.SettingsPresenter;
import com.example.mycareer.presenter.SettingsPresenterImpl;
import com.example.mycareer.utils.Constants;
import com.example.mycareer.utils.SharedPrefManager;
import com.example.mycareer.utils.Utils;
import com.example.mycareer.view.SettingsView;

import java.util.Locale;

import androidx.cardview.widget.CardView;


public class SettingsActivity  extends BaseActivity implements SettingsView {
    private SettingsPresenter settingsPresenter;

    private CardView cardViewTotCredits;
    private CardView cardViewLaudeVal;
    private Dialog dialog;
    private TextView textView_totCreditsVal;
    private TextView textView_laudeVal;
    private Button button_save;

    private TextView textView_ok;
    private TextView textView_cancel;
    private TextView textView_dialogTitle;
    private EditText editText;

    private int dialogRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        settingsPresenter = new SettingsPresenterImpl(this);
        settingsPresenter.attachView(this);

        initUI();
        initCustomDialog();
        initListeners();
    }

    private void initUI() {
        cardViewTotCredits = findViewById(R.id.cardViewTotCredits);
        cardViewLaudeVal = findViewById(R.id.cardViewLaudeVal);

        textView_totCreditsVal = findViewById(R.id.textView_totCreditsVal);
        textView_laudeVal = findViewById(R.id.textView_laudeVal);
        button_save = findViewById(R.id.button_save);

        setTotCredits(SharedPrefManager.getIntPrefVal(this, Constants.Strings.SPREF_TOT_CREDITS_KEY, Profile.getInstance().getStatistics().getTotalCfu()));
        setLaudeVal(SharedPrefManager.getIntPrefVal(this, Constants.Strings.SPREF_LAUDE_VALUE_KEY, 30));
    }

    private void initListeners() {
        cardViewTotCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.setOnClickListenerCardViewTotCredits();
            }
        });
        cardViewLaudeVal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.setOnClickListenerCardViewLaudeVal();
            }
        });
        button_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.setOnClickListenerButtonSave();
            }
        });

        textView_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.setOnClickListenerTextViewOk(dialogRequest);
            }
        });
        textView_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsPresenter.setOnClickListenerTextViewCancel();
            }
        });
    }

    private void initCustomDialog() {
        // custom dialog
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.textview_dialog);

        textView_dialogTitle = dialog.findViewById(R.id.textView_dialogTitle);
        editText = dialog.findViewById(R.id.editText_number);
        textView_ok = dialog.findViewById(R.id.textView_ok);
        textView_cancel = dialog.findViewById(R.id.textView_cancel);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void showDialog(String title, int req) {
        textView_dialogTitle.setText(title);
        editText.setText("");
        dialog.show();

        this.dialogRequest = req;
    }

    @Override
    public void dismissDialog() {
        dialog.dismiss();
    }

    @Override
    public void setTotCredits(int credits) {
        textView_totCreditsVal.setText(String.format(Locale.getDefault(),"%d", credits));
    }

    @Override
    public void setLaudeVal(int val) {
        textView_laudeVal.setText(String.format(Locale.getDefault(),"%d", val));
    }

    @Override
    public CharSequence getEditTextVal() {
        return editText.getText();
    }

    @Override
    public void onError(String error) {
        Utils.showMessage(this, error);
    }

    @Override
    public void refreshApp() {
        Utils.setIntent(getContext(), SplashActivity.class);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
