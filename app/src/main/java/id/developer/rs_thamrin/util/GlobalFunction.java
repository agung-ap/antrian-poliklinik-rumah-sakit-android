package id.developer.rs_thamrin.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class GlobalFunction {

    public void addTokenAndUserIdPref(Context context,String token, String userId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("credential",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("token", token);
        editor.putString("userId", userId);
        editor.apply();
    }
}
