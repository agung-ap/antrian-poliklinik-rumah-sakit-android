package id.developer.rs_thamrin.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.util.GlobalFunction;
import me.pushy.sdk.Pushy;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private static final String TAG = SplashScreen.class.getName();
    private final int splash_display_length = 3000;

    private ProgressBar progressBar;
    private SharedPreferences preference;
    private String token, deviceToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);
        progressBar = (ProgressBar)findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#455ede"),android.graphics.PorterDuff.Mode.MULTIPLY);


        preference = getSharedPreferences(getString(R.string.GET_CREDENTIAL), Context.MODE_PRIVATE);
        token = preference.getString(getString(R.string.GET_USER_TOKEN),"default");

        // Not registered yet?
        if (getDeviceToken() == null) {
            // Register with Pushy
            new RegisterForPushNotificationsAsync().execute();
        }
        else {
            // Start Pushy notification service if not already running
            Pushy.listen(this);
            // Check whether the user has granted us the READ/WRITE_EXTERNAL_STORAGE permissions
            if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                // Request both READ_EXTERNAL_STORAGE and WRITE_EXTERNAL_STORAGE so that the
                // Pushy SDK will be able to persist the device token in the external storage
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
            }

            // Update UI with device token
            updateUI();
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSession(token, getDeviceToken());

            }
        },splash_display_length);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void getSession(String token, String deviceToken){
        if (token.equals("default")){
            GlobalFunction.moveActivity(SplashScreen.this, MainActivity.class);
            finish();
        }else {
            LoginApi loginApi = RetrofitBuilder.getApiService().create(LoginApi.class);
            Call<ResponseBody> callSessionApi = loginApi.sessionCheck(token, deviceToken);
            callSessionApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getInt("code") == 1){
                            progressBar.setVisibility(View.GONE);

                            GlobalFunction.moveActivity(SplashScreen.this, HomeActivity.class);
                            GlobalFunction.toast(SplashScreen.this, object.getString("info"));
                            finish();
                        }else {
                            progressBar.setVisibility(View.GONE);

                            GlobalFunction.moveActivity(SplashScreen.this, MainActivity.class);
                            GlobalFunction.toast(SplashScreen.this, object.getString("info"));
                            finish();
                        }

                    } catch (JSONException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    } catch (IOException e) {
                        progressBar.setVisibility(View.GONE);
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    GlobalFunction.moveActivity(SplashScreen.this, MainActivity.class);
                    finish();
                }
            });
        }
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<String, Void, Exception> {

        @Override
        protected Exception doInBackground(String... params) {
            try {
                // Assign a unique token to this device
                String deviceToken = Pushy.register(SplashScreen.this);

                // Save token locally / remotely
                saveDeviceToken(deviceToken);
            }
            catch (Exception exc) {
                // Return exc to onPostExecute
                return exc;
            }

            // Success
            return null;
        }

        @Override
        protected void onPostExecute(Exception exc) {
            // Activity died?
            if (isFinishing()) {
                return;
            }

            // Registration failed?
            if (exc != null) {
                // Write error to logcat
                Log.e(TAG, "Registration failed: " + exc.getMessage());
            }

            // Update UI with registration result
            updateUI();
        }
    }

    private void updateUI() {
        // Get device token from SharedPreferences
        deviceToken = getDeviceToken();

        // Registration failed?
        if (deviceToken == null) {
            Log.e(TAG, "failed generate token ");
            // Stop execution
            return;
        }
    }

    private String getDeviceToken() {
        // Get token stored in SharedPreferences
        return getSharedPreferences().getString("deviceToken", null);
    }

    private void saveDeviceToken(String deviceToken) {
        // Save token locally in app SharedPreferences
        getSharedPreferences().edit().putString("deviceToken", deviceToken).apply();
    }

    private SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(this);
    }
}
