package id.developer.rs_thamrin.Fragment.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import id.developer.rs_thamrin.Fragment.user.PoliklinikRegisterResultFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.QueueApi;
import id.developer.rs_thamrin.api.RegisterApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.request.UserApproveRequest;
import id.developer.rs_thamrin.model.response.QueueResponse;
import id.developer.rs_thamrin.model.response.UserApprovalResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserApproveFragment extends Fragment {
    private EditText userId;
    private Button approve;

    private SharedPreferences preferences;
    private String userRole, token;

    public UserApproveFragment() {
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
        View view = inflater.inflate(R.layout.fragment_user_approve, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Approval");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(false);

        bindView(view);
        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleApprove();
            }
        });


        return view;
    }

    private void bindView(View view){
        userId = view.findViewById(R.id.approve_user_id);
        approve = view.findViewById(R.id.approve);
    }

    private void handleApprove(){
        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.checkingQueueNumber(token, userId.getText().toString().trim());
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0){
                        UserApprovalResponse approvalResponse = new UserApprovalResponse();
                        approvalResponse.setInfo(object.getString("info"));

                        sendResponseToAnotherFragment(approvalResponse);
                    }else {
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

            }
        });
    }

    private void sendResponseToAnotherFragment(UserApprovalResponse response){
        Bundle bundle = new Bundle();
        ArrayList<UserApprovalResponse> queueResponseList = new ArrayList<>();
        queueResponseList.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), queueResponseList);

        UserApprovalResultFragment fragment = new UserApprovalResultFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "poliklinik_register_result_fragment")
                .commit();
    }

}
