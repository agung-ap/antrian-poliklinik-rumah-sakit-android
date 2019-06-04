package id.developer.rs_thamrin.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashScreen extends AppCompatActivity {
    private final int splash_display_length = 3000;

    private SharedPreferences preference;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash_screen);

        preference = getSharedPreferences(getString(R.string.GET_CREDENTIAL), Context.MODE_PRIVATE);
        token = preference.getString(getString(R.string.GET_USER_TOKEN),"default");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getSession(token);
            }
        },splash_display_length);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    private void getSession(String token){
        if (token.equals("default")){
            GlobalFunction.moveActivity(SplashScreen.this, MainActivity.class);
            finish();
        }else {
            LoginApi loginApi = RetrofitBuilder.getApiService().create(LoginApi.class);
            Call<ResponseBody> callSessionApi = loginApi.sessionCheck(token);
            callSessionApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getInt("code") == 1){
                            GlobalFunction.moveActivity(SplashScreen.this, HomeActivity.class);
                            GlobalFunction.toast(SplashScreen.this, object.getString("info"));
                            finish();
                        }else {
                            GlobalFunction.moveActivity(SplashScreen.this, MainActivity.class);
                            GlobalFunction.toast(SplashScreen.this, object.getString("info"));
                            finish();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
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
}
