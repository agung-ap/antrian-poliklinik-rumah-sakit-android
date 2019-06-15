package id.developer.rs_thamrin.Fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.activity.MainActivity;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.request.LoginRequest;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment implements View.OnClickListener{
    private EditText userId;
    private EditText password;
    private Button login;
    private Button register;

    private ProgressDialog progressDialog;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)  {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Login");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        bindView(view);

        return view;
    }

    private void bindView(View view){
        userId = (EditText)view.findViewById(R.id.userId);
        password = (EditText)view.findViewById(R.id.password);
        login = (Button)view.findViewById(R.id.login);
        register = (Button)view.findViewById(R.id.register_button);

        login.setOnClickListener(this);
        register.setOnClickListener(this);
    }

    private void handleLogin(){
        if (TextUtils.isEmpty(userId.getText().toString())){
            userId.setError("user id tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(password.getText().toString())){
            password.setError("password tidak boleh kosong");
            return;
        }

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUserId(userId.getText().toString().trim());
        loginRequest.setPassword(password.getText().toString().trim());

        LoginApi loginApi = RetrofitBuilder.getApiService().create(LoginApi.class);
        Call<ResponseBody> callLoginApi = loginApi.setLogin(loginRequest);
        callLoginApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    String info = object.getString("info");

                    if (object.getInt("code") == 1){
                        progressDialog.dismiss();
                        JSONObject jsonObject = object.getJSONObject("data");

                        String token = jsonObject.getString("token");
                        String userRole = jsonObject.getString("userRole");

                        Log.i("TAG", "userRole : " + userRole);
                        GlobalFunction.addTokenAndUserRolePref(getActivity(), token,userRole);

                        if (userRole.equals("USER")){
                            GlobalFunction.toast(getActivity(), info);
                            GlobalFunction.moveActivity(getActivity(), HomeActivity.class);
                            getActivity().finish();
                        }
                        if (userRole.equals("ADMIN")){
                            GlobalFunction.toast(getActivity(), info);
                            GlobalFunction.moveActivity(getActivity(), HomeActivity.class);
                            getActivity().finish();
                        }
                        if (userRole.equals("DOKTER")){
                            GlobalFunction.toast(getActivity(), info);
                            GlobalFunction.moveActivity(getActivity(), HomeActivity.class);
                            getActivity().finish();
                        }
                    }else {
                        progressDialog.dismiss();
                        Toast.makeText(getActivity(), object.getString("info"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }



    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.login){
            handleLogin();
        }

        if (v.getId() == R.id.register_button){
            getFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, new RegisterFragment(), "NewFragmentTag")
                    .addToBackStack(null)
                    .commit();
        }
    }


}
