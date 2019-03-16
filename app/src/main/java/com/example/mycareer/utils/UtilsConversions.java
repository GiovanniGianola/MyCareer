package com.example.mycareer.utils;

import android.app.Application;

import java.util.Locale;

public class UtilsConversions<Data>  {

    public static String convertScoreToString(int num){
        String n = "";
        switch(num){
            case 17:
                n = "ND";
                break;
            case 31:
                n = "30L";
                break;
            default:
                n = String.format(Locale.getDefault(),"%s", num);
        }
        return n;
    }

    public static int convertScoreToInt(String num){
        int n = 0;
        switch(num){
            case Constants.Strings.NOT_DONE_YET:
                n = 17;
                break;
            case "30L":
                n = 31;
                break;
            default:
                n = Integer.parseInt(num);
        }
        return n;
    }

    public static double convertScoreToDouble(String num){
        double n = 0;
        double laude = SharedPrefManager.getIntPrefVal(MyApplication.getAppContext(), Constants.Strings.SPREF_LAUDE_VALUE_KEY, 30);
        switch(num){
            case Constants.Strings.NOT_DONE_YET:
                n = -1D;
                break;
            case "30L":
                n = laude;
                break;
            default:
                n = Double.parseDouble(num);
        }
        return n;
    }
}
