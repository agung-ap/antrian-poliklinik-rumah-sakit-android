package id.developer.rs_thamrin.Fragment.user;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.support.v7.widget.AppCompatSpinner;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.developer.rs_thamrin.Fragment.HomeFragment;
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
    private EditText dateQueue;
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
    private String date;
    private boolean status = true;

    private View view;

    private ProgressDialog progressDialog;
    private Calendar myCalendar = Calendar.getInstance();

    //cancel queue view
    private String info, code, time;

    private TextView message;
    private TextView queueCode;
    private TextView queuedate;

    private Button cancelQueue;

    public PoliklinikRegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");

        status = getArguments().getBoolean("status");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (status){
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_cancel_queue, container, false);
            setHasOptionsMenu(false);
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Status Antrian");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            bindViewQueue(view);

            cancelQueue.setOnClickListener(this);
        }else {
            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_poliklinik_register, container, false);
            setHasOptionsMenu(false);
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Daftar Antrian Poliklinik");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            bindView(view);
            dateQueue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePickerDialog();
                }
            });

            getPaymentTypeCode();
            getPoliklinikCode();

            register.setOnClickListener(this);
        }



        return view;
    }

    private void bindViewQueue(View view){
        message = view.findViewById(R.id.message);
        queueCode = view.findViewById(R.id.queue_code);
        queuedate = view.findViewById(R.id.date);

        message.setText(getArguments().getString(getString(R.string.GET_INFO)));
        queueCode.setText(getArguments().getString(getString(R.string.GET_CODE)));
        queuedate.setText("Tanggal " + getArguments().getString(getString(R.string.GET_DATE)));

        cancelQueue = view.findViewById(R.id.cancel_queue);
    }

    private void bindView(View view){
        paymentNumber = view.findViewById(R.id.payment_number);
        dateQueue = view.findViewById(R.id.date_queue);
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
        request.setDate(date);

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
                    }else {
                        progressDialog.dismiss();
                        GlobalFunction.toast(getActivity(),object.getString("info"));

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
        if (v.getId() == R.id.cancel_queue){
            deleteQueue(token);
        }

    }

    final DatePickerDialog.OnDateSetListener mdate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dateQueue.setText(sdf.format(myCalendar.getTime()));
            date = sdf.format(myCalendar.getTime());
        }
    };

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void deleteQueue(String token){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Membatalkan Antrian...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.cancelQueue(token);
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();

                try {
                    JSONObject object = new JSONObject(response.body().string());
                    GlobalFunction.toast(getActivity(), object.getString("info"));

                    getFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout_home, new HomeFragment(), "home_fragment")
                            .commit();
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
