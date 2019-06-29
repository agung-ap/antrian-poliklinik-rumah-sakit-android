package id.developer.rs_thamrin.Fragment.dokter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.Fragment.admin.UserApprovalResultFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.QueueApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.master.DoctorData;
import id.developer.rs_thamrin.model.response.QueueListResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueListDetailFragment extends Fragment {
    private List<QueueListResponse> queueListResponse = new ArrayList<>();
    private SharedPreferences preferences;
    private String token;

    private ProgressDialog progressDialog;

    private TextView userId, patien, paymentType, polilinik;
    private Button callPatien, doneChecking;

    public QueueListDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queueListResponse = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue_list_detail, container, false);
        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Detail Antrian");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);
        getDetailData();
        callPatien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPatien();
            }
        });
        doneChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doneChecking();
            }
        });

        return view;
    }

    private void bindView(View view){
        userId = view.findViewById(R.id.user_id_queue);
        patien = view.findViewById(R.id.patien_queue);
        paymentType = view.findViewById(R.id.payment_queue);
        polilinik = view.findViewById(R.id.poliklinik_queue);

        callPatien = view.findViewById(R.id.call_patien);
        doneChecking = view.findViewById(R.id.done_check);

        callPatien.setEnabled(true);
        doneChecking.setEnabled(false);
    }

    private void getDetailData(){
        userId.setText(queueListResponse.get(0).getUserId());
        patien.setText(queueListResponse.get(0).getUsername());
        paymentType.setText(queueListResponse.get(0).getPaymentType());
        polilinik.setText(queueListResponse.get(0).getPoliklinik());
    }

    private void callPatien(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.callPatien(token, queueListResponse.get(0).getUserId());
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressDialog.dismiss();
                        callPatien.setEnabled(false);
                        doneChecking.setEnabled(true);

                        GlobalFunction.showMessage(getActivity(),object.getString("info"));
                    }else {
                        progressDialog.dismiss();
                        GlobalFunction.showMessage(getActivity(),object.getString("info"));
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

    private void doneChecking(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.doneChecking(token, queueListResponse.get(0).getUserId());
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressDialog.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(object.getString("info"));
                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK button
                                FragmentManager manager = getActivity().getSupportFragmentManager();
                                FragmentTransaction trans = manager.beginTransaction();

                                if (manager.getBackStackEntryCount() > 0) {
                                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                                }

                                trans.remove(new QueueListDetailFragment());
                                trans.commit();
                            }
                        });
                        builder.create().show();
                    }else {
                        progressDialog.dismiss();
                        GlobalFunction.showMessage(getActivity(),object.getString("info"));
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

}
