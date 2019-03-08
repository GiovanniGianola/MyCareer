package com.example.mycareer.utils;

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
                n = num+"";
        }
        return n;
    }

    public static int convertScoreToInt(String num){
        int n = 0;
        switch(num){
            case Costants.Strings.NOT_DONE_YET:
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
}
