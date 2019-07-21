package id.developer.rs_thamrin.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import id.developer.rs_thamrin.Fragment.admin.UserApprovalResultFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.request.ChangePasswordRequest;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordFragment extends Fragment {
    private SharedPreferences preferences;
    private String userRole, token;

    private EditText oldPassword, newPassword, newConfirmPassword;
    private Button changePassword;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Ganti Password");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);

        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setChangePassword();
            }
        });

        return view;
    }

    private void bindView(View view){
        changePassword = view.findViewById(R.id.change_password);

        oldPassword = view.findViewById(R.id.old_password);
        newPassword = view.findViewById(R.id.new_password);
        newConfirmPassword = view.findViewById(R.id.new_confirm_password);
    }

    private void setChangePassword(){
        if (TextUtils.isEmpty(oldPassword.getText().toString())){
            oldPassword.setError("user id tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(newPassword.getText().toString())){
            newPassword.setError("password tidak boleh kosong");
            return;
        }

        if (TextUtils.isEmpty(newConfirmPassword.getText().toString())){
            newConfirmPassword.setError("password tidak boleh kosong");
            return;
        }

        if (newPassword.getText().toString().trim().length() != newConfirmPassword.getText().toString().trim().length()){
            GlobalFunction.toast(getActivity(), "konfirmasi password tidak sama dengan password");
            return;
        }

        if (newPassword.getText().toString().trim().length() < 8){
            newPassword.setError("password kurang dari 8 karakter");
            return;
        }

        if (newConfirmPassword.getText().toString().trim().length() < 8){
            newConfirmPassword.setError("password kurang dari 8 karakter");
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        ChangePasswordRequest request = new ChangePasswordRequest();
        request.setOldPassword(oldPassword.getText().toString().trim());
        request.setNewPassword(newConfirmPassword.getText().toString().trim());

        LoginApi loginApi = RetrofitBuilder.getApiService().create(LoginApi.class);
        Call<ResponseBody> callLoginApi = loginApi.changePassword(token,request);
        callLoginApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0){
                        progressDialog.dismiss();
                        GlobalFunction.toast(getActivity(), object.getString("info"));
                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();

                        if (manager.getBackStackEntryCount() > 0) {
                            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(0);
                            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        trans.remove(new ChangePasswordFragment());
                        trans.commit();

                    }else {
                        progressDialog.dismiss();
                        GlobalFunction.toast(getActivity(), object.getString("info"));
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
}
