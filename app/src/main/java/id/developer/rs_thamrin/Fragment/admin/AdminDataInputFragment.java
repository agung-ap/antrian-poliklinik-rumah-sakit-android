package id.developer.rs_thamrin.Fragment.admin;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RegisterApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.AdminData;
import id.developer.rs_thamrin.model.request.AdminRegisterRequest;
import id.developer.rs_thamrin.model.response.AdminRegisterResponse;
import id.developer.rs_thamrin.model.response.DoctorDataResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDataInputFragment extends Fragment {
    private SharedPreferences preferences;
    private String userRole, token;
    private String specializationCode;

    private boolean isEdit = false;
    private List<AdminData> adminDataList;

    private EditText userId;
    private EditText firstName;
    private EditText lastName;
    private EditText birthPlace;
    private EditText birthDate;
    private EditText address;

    private Calendar myCalendar = Calendar.getInstance();

    private Button save;

    public AdminDataInputFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");

        isEdit = getArguments().getBoolean("isEdit", false);
        adminDataList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_data_input, container, false);
        if (isEdit){
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Edit Admin");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Input Admin");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bindView(view);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        if (isEdit){
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd").parse(adminDataList.get(0).getBirthDate());
                birthDate.setText(new SimpleDateFormat("dd MMM yyyy").format(d));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            userId.setText(String.valueOf(adminDataList.get(0).getUserId()));
            firstName.setText(String.valueOf(adminDataList.get(0).getFirstName()));
            lastName.setText(String.valueOf(adminDataList.get(0).getLastName()));
            birthPlace.setText(String.valueOf(adminDataList.get(0).getBirthPlace()));
            address.setText(String.valueOf(adminDataList.get(0).getAddress()));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleAdminRegister();
            }
        });


        return view;
    }

    private void bindView(View view) {
        userId = view.findViewById(R.id.user_id_admin);
        firstName = view.findViewById(R.id.first_name_admin);
        lastName = view.findViewById(R.id.last_name_admin);
        birthDate = view.findViewById(R.id.birth_date_admin);
        birthPlace = view.findViewById(R.id.birth_place_admin);
        address = view.findViewById(R.id.address_admin);

        save = view.findViewById(R.id.admin_input_save);
    }

    final DatePickerDialog.OnDateSetListener mdate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy");
            birthDate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void handleAdminRegister(){

        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        if (TextUtils.isEmpty(userId.getText().toString())){
            userId.setError("user id tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(firstName.getText().toString())){
            userId.setError("first name tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(lastName.getText().toString())){
            userId.setError("last name tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(birthDate.getText().toString())){
            userId.setError("last name tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(birthPlace.getText().toString())){
            userId.setError("last name tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        if (TextUtils.isEmpty(address.getText().toString())){
            userId.setError("address tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        AdminRegisterRequest request = new AdminRegisterRequest();
        request.setAdminId(userId.getText().toString().trim());
        request.setFirstName(firstName.getText().toString().trim());
        request.setLastName(lastName.getText().toString().trim());
        request.setBirthPlace(birthPlace.getText().toString().trim());
        try {
            Date d = new SimpleDateFormat("dd MMM yyyy").parse(birthDate.getText().toString().trim());
            request.setBirthDate(new SimpleDateFormat("yyyy-MM-dd").format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        request.setAddress(address.getText().toString().trim());

        if (isEdit){
            DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
            Call<ResponseBody> callDataApi = dataApi.updateAdmin(adminDataList.get(0).getId(), token, request);
            callDataApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getInt("code") == 2){
                            progressDialog.dismiss();
                            GlobalFunction.toast(getActivity(), object.getString("info"));

                            FragmentManager manager = getActivity().getSupportFragmentManager();
                            FragmentTransaction trans = manager.beginTransaction();

                            if (manager.getBackStackEntryCount() > 0) {
                                FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                                manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                            }

                            trans.remove(new MasterDataDoctorInputFragment());
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

        }else {
            RegisterApi registerApi = RetrofitBuilder.getApiService().create(RegisterApi.class);
            Call<ResponseBody> callRegisterApi = registerApi.setAdmin(token, request);
            callRegisterApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getInt("code") == 2){
                            progressDialog.dismiss();
                            JSONObject jsonObject = object.getJSONObject("data");

                            AdminRegisterResponse dataResponse = new AdminRegisterResponse();
                            dataResponse.setInfo(object.getString("info"));
                            dataResponse.setAdminId(jsonObject.getString("adminId"));
                            dataResponse.setCreatedDate(jsonObject.getString("createdDate"));
                            dataResponse.setName(jsonObject.getString("name"));
                            dataResponse.setPassword(jsonObject.getString("password"));

                            sendResponseToAnotherFragment(dataResponse);
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

    private void sendResponseToAnotherFragment(AdminRegisterResponse response){
        Bundle bundle = new Bundle();
        ArrayList<AdminRegisterResponse> list = new ArrayList<>();
        list.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), list);

        AdminDataInputResultFragment fragment = new AdminDataInputResultFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "admin_register_result_fragment")
                .addToBackStack(null)
                .commit();
    }
}
