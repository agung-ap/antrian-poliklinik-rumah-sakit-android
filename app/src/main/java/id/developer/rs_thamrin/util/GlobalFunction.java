package id.developer.rs_thamrin.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import id.developer.rs_thamrin.R;

import static android.content.ContentValues.TAG;

public class GlobalFunction {

    public static void addTokenAndUserRolePref(Context context, String token, String userRole){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.GET_CREDENTIAL),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.GET_USER_TOKEN), token);
        editor.putString(context.getString(R.string.GET_USER_ROLE), userRole);
        editor.apply();
    }

    public static void toast(Context context, String msg ){
        Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
    }

    public static void moveActivity(Activity activity, Class directionClass){
        activity.startActivity(new Intent(activity, directionClass));
    }

    public static void moveActivity(Activity activity, Class directionClass, String role){
        Intent intent = new Intent(activity,directionClass);
        intent.putExtra(activity.getString(R.string.GET_USER_ROLE), role);
        activity.startActivity(intent);
    }

    public static void addInfoCodeAndTimeResponse(Context context, String info, String code, String date){
        SharedPreferences sharedPreferences = context.getSharedPreferences(context.getString(R.string.RESPONSE),context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(context.getString(R.string.GET_INFO), info);
        editor.putString(context.getString(R.string.GET_CODE), code);
        editor.putString(context.getString(R.string.GET_DATE), date);
        editor.apply();
    }
}
