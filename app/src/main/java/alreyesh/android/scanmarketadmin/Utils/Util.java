package alreyesh.android.scanmarketadmin.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

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

    public static String getidProduct(SharedPreferences preferences){
        return preferences.getString("idproduct","0.0");
    }
    public static String getimagenProduct(SharedPreferences preferences){
        return preferences.getString("imagenproduct","");
    }
    public static String getnombreProduct(SharedPreferences preferences){
        return preferences.getString("nameproduct","");
    }
    public static String getDescripcionProduct(SharedPreferences preferences){
        return preferences.getString("descripcionproduct","");
    }
    public static String getCategoriaProduct(SharedPreferences preferences){
        return preferences.getString("categoriaproduct","");
    }
    public static String getcodigoProduct(SharedPreferences preferences){
        return preferences.getString("codigoproduct","");
    }
    public static String getprecioProduct(SharedPreferences preferences){
        return preferences.getString("precioproduct","");
    }
    public static String getdescuentoProduct(SharedPreferences preferences){
        return preferences.getString("descuentoproduct","");
    }




    public static void removeSharedPreferences(SharedPreferences preferences){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("email");
        editor.remove("pass");
        editor.apply();
    }
    public static String getJson(SharedPreferences preferences){
        return preferences.getString("json","");
    }
    public static SharedPreferences getSP(Context context){
        String masterKeyAlias = null;
        try {
            masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            return EncryptedSharedPreferences.create(
                    "Preferences",
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch ( Exception e) {

        }

        return null;
    }




}
