package alreyesh.android.scanmarketadmin.Utils;

import android.content.SharedPreferences;

public class Util {

    public  static String getUserAccount(SharedPreferences preferences){
        return preferences.getString("account","");
    }
    public static String getUserMailPrefs(SharedPreferences preferences){
        return preferences.getString("email","");
    }


    public static String getUserPassPrefs(SharedPreferences preferences){
        return preferences.getString("pass","");
    }
    public static void removeSharedPreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("pass");
        editor.apply();
    }




}
