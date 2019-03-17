package com.example.mycareer.view.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.mycareer.R;
import com.example.mycareer.base.BaseActivity;
import com.example.mycareer.presenter.AppInfoPresenter;
import com.example.mycareer.presenter.AppInfoPresenterImpl;
import com.example.mycareer.view.AppInfoView;

import androidx.cardview.widget.CardView;

public class AppInfoActivity extends BaseActivity implements AppInfoView {
    private AppInfoPresenter appInfoPresenter;

    private CardView cardViewAbout;
    private CardView cardViewEraseData;
    private CardView cardViewSendEmail;
    private TextView textViewVersion;

    private AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_info);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        appInfoPresenter = new AppInfoPresenterImpl(this);
        appInfoPresenter.attachView(this);

        initUI();
        initListeners();
    }


    private void initUI() {
        cardViewAbout = findViewById(R.id.cardViewAbout);
        cardViewEraseData = findViewById(R.id.cardViewEraseData);
        cardViewSendEmail = findViewById(R.id.cardViewSendEmail);
        textViewVersion = findViewById(R.id.textView_version);
        try {
            textViewVersion.setText("V. " + this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        builder.setTitle(R.string.app_info_erase_data).setMessage(R.string.app_info_erase_data_message);
    }

    private void initListeners() {
        cardViewAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appInfoPresenter.setOnClickListenerCardViewAbout();
            }
        });
        cardViewEraseData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appInfoPresenter.setOnClickListenerCardViewEraseData();
            }
        });
        cardViewSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appInfoPresenter.setOnClickListenerCardViewSendEmail();
            }
        });
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onAbout() {

    }

    @Override
    public void onEraseData() {
        builder
            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    onEraseData();
                }
            })
            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // do nothing
                }
            })
            .setIcon(android.R.drawable.ic_dialog_alert)
            .show();
    }

    @Override
    public void onSendEmail() {
        String body = null;
        try {
            body = this.getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
            body = "\n\n-----------------------------\nPlease don't remove this information\n Device OS: Android \n Device OS version: " +
                    Build.VERSION.RELEASE + "\n App Version: " + body + "\n Device Brand: " + Build.BRAND +
                    "\n Device Model: " + Build.MODEL + "\n Device Manufacturer: " + Build.MANUFACTURER;
        } catch (PackageManager.NameNotFoundException e) {
        }
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("message/rfc822");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"g.gianola.test@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "Query from android app");
        intent.putExtra(Intent.EXTRA_TEXT, body);
        this.startActivity(Intent.createChooser(intent, this.getString(R.string.choose_email_client)));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
