package id.developer.rs_thamrin.Fragment.user;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.support.v7.widget.AppCompatSpinner;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.QueueApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.Poliklinik;
import id.developer.rs_thamrin.model.master.TypeOfPayment;
import id.developer.rs_thamrin.model.request.QueueRequest;
import id.developer.rs_thamrin.model.response.QueueResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


public class PoliklinikRegisterFragment extends Fragment implements View.OnClickListener {

    private EditText paymentNumber;
    private Button register;

    private SharedPreferences preferences;

    private String paymentCode;
    private String poliklinikCode;
    private AppCompatSpinner paymentTypeSpinner;
    private AppCompatSpinner poliklinikCodeSpinner;

    private List<String> paymentTypeName;
    private List<TypeOfPayment> paymentTypeList;

    private List<String> poliklinikName;
    private List<Poliklinik> poliklinikList;

    private String userRole;
    private String token;

    private ProgressDialog progressDialog;

    public PoliklinikRegisterFragment() {
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
        View view = inflater.inflate(R.layout.fragment_poliklinik_register, container, false);
        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Daftar Poliklinik");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);
        getPaymentTypeCode();
        getPoliklinikCode();

        register.setOnClickListener(this);
        return view;
    }

    private void bindView(View view){
        paymentNumber = view.findViewById(R.id.payment_number);
        register = view.findViewById(R.id.poliklinik_register);

        paymentTypeSpinner = view.findViewById(R.id.payment_type);
        poliklinikCodeSpinner = view.findViewById(R.id.poliklinik_code);
    }

    private void getPaymentTypeCode(){
        paymentTypeName = new ArrayList<String>();
        paymentTypeList = new ArrayList<TypeOfPayment>();

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getPayment();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfPayment payment = new TypeOfPayment();
                            payment.setCode(jsonObject.getString("code"));
                            payment.setName(jsonObject.getString("name"));

                            paymentTypeName.add(jsonObject.getString("name"));
                            paymentTypeList.add(payment);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                paymentTypeName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        paymentTypeSpinner.setAdapter(adapter);

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

        paymentTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentCode = paymentTypeList.get(position).getCode();
                Log.i(TAG, "paymentTypeSpinner: " + paymentCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getPoliklinikCode(){
        poliklinikName = new ArrayList<>();
        poliklinikList = new ArrayList<>();

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getPoliklinik();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Poliklinik poliklinik = new Poliklinik();
                            poliklinik.setCode(jsonObject.getString("code"));
                            poliklinik.setPoliklinikName(jsonObject.getString("poliklinikName"));

                            poliklinikName.add(jsonObject.getString("poliklinikName") + " "+ jsonObject.getString("time"));
                            poliklinikList.add(poliklinik);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                poliklinikName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        poliklinikCodeSpinner.setAdapter(adapter);
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

        poliklinikCodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                poliklinikCode = poliklinikList.get(position).getCode();
                Log.i(TAG, "poliklinikCodeSpinner: " + poliklinikCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void handleRegister(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        QueueRequest request = new QueueRequest();
        request.setPaymentNumber(paymentNumber.getText().toString().trim());
        request.setPaymentType(paymentCode);
        request.setPoliklinik(poliklinikCode);

        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.setQueue(token, request);
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressDialog.dismiss();
                        JSONObject jsonObject = object.getJSONObject("data");

                        QueueResponse queueResponse = new QueueResponse();
                        queueResponse.setMessage(jsonObject.getString("message"));
                        queueResponse.setPoliklinikName(jsonObject.getString("poliklinikName"));
                        queueResponse.setDoctorName(jsonObject.getString("doctorName"));
                        queueResponse.setCustomerName(jsonObject.getString("customerName"));
                        queueResponse.setSchedule(jsonObject.getString("schedule"));
                        queueResponse.setRangeTime(jsonObject.getString("rangeTime"));
                        queueResponse.setQueueCode(jsonObject.getString("queueCode"));

                        sendResponseToAnotherFragment(queueResponse);
                    }else if (object.getInt("code") == 2){
                        progressDialog.dismiss();
                        GlobalFunction.toast(getActivity(), object.getString("info"));
                    }
                    else {
                        progressDialog.dismiss();
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

    private void sendResponseToAnotherFragment(QueueResponse response){
        Bundle bundle = new Bundle();
        ArrayList<QueueResponse> queueResponseList = new ArrayList<>();
        queueResponseList.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), queueResponseList);

        PoliklinikRegisterResultFragment fragment = new PoliklinikRegisterResultFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "poliklinik_register_result_fragment")
                .commit();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.poliklinik_register){
            handleRegister();
        }

    }
}
