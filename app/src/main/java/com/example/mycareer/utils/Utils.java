package com.example.mycareer.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import com.google.android.material.snackbar.Snackbar;
import android.view.View;
import android.widget.Toast;

public class Utils<Data> {

    public static Intent setIntent(Context context, Class destination) {

        Intent intent = new Intent(context, destination);
        context.startActivity(intent);

        return intent ;
    }

    public Intent setIntentExtra(Context context, Class destination, String key, Data data) {
        Intent intent = new Intent(context, destination);
        intent.putExtra(key, (Parcelable) data);
        context.startActivity(intent);

        return intent ;
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }
}
