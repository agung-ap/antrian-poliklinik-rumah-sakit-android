package id.developer.rs_thamrin.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
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

    public static void showMessage(Context context,String info){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(info);

        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked OK button
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
