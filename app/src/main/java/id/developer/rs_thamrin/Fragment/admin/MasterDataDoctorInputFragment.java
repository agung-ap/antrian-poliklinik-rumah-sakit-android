package id.developer.rs_thamrin.Fragment.admin;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatSpinner;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RegisterApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.DoctorData;
import id.developer.rs_thamrin.model.master.TypeOfSpecialization;
import id.developer.rs_thamrin.model.request.DoctorDataRequest;
import id.developer.rs_thamrin.model.response.DoctorDataResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataDoctorInputFragment extends Fragment {
    private SharedPreferences preferences;
    private String userRole, token;
    private String specializationCode;

    private boolean isEdit = false;
    private List<DoctorData> doctorList;

    private EditText userId;
    private EditText firstName;
    private EditText lastName;
    private EditText address;

    private AppCompatSpinner specialization;

    private List<String> specializationName;
    private List<TypeOfSpecialization> specializationList;

    private Calendar myCalendar = Calendar.getInstance();

    private Button save;

    public MasterDataDoctorInputFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");

        isEdit = getArguments().getBoolean("isEdit", false);
        doctorList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master_data_doctor_input, container, false);
        if (isEdit){
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Edit Dokter");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Input Dokter");
            ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        bindView(view);
        getSpecialization();

        if (isEdit){
            userId.setText(String.valueOf(doctorList.get(0).getUserId()));
            firstName.setText(String.valueOf(doctorList.get(0).getFirsName()));
            lastName.setText(String.valueOf(doctorList.get(0).getLastName()));
            address.setText(String.valueOf(doctorList.get(0).getAddress()));
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctorRegister();
            }
        });


        return view;
    }

    private void bindView(View view) {
        userId = view.findViewById(R.id.user_id_dokter);
        firstName = view.findViewById(R.id.first_name_dokter);
        lastName = view.findViewById(R.id.last_name_dokter);
        address = view.findViewById(R.id.address_dokter);

        specialization = view.findViewById(R.id.specialization_dokter);

        save = view.findViewById(R.id.dokter_input_save);
    }

    private void getSpecialization(){
        specializationName = new ArrayList<>();
        specializationList = new ArrayList<>();

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getSpecialization();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        JSONArray jsonArray = object.getJSONArray("data");
                        if (!isEdit){
                            specializationList.add(null);
                            specializationName.add("");
                        }else {
                            specializationName.add("");
                        }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfSpecialization specialization = new TypeOfSpecialization();
                            specialization.setName(jsonObject.getString("name"));
                            specialization.setCode(jsonObject.getString("code"));

                            specializationName.add(specialization.getName());
                            specializationList.add(specialization);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                specializationName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        specialization.setAdapter(adapter);
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

        specialization.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (isEdit){
                    if (position == 0){
                        specialization.setSelection(doctorList.get(0).getSpecializationId());
                        specializationCode = specializationList.get(position).getCode();
                    }else {
                        specialization.setSelection(position, true);
                        specializationCode = specializationList.get(position - 1).getCode();
                    }
                }else {
                    if (position == 0){
                        specialization.setSelection(position, true);
                        specializationCode = "";
                    }else {
                        specializationCode = specializationList.get(position).getCode();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleDoctorRegister(){
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

        if (TextUtils.isEmpty(address.getText().toString())){
            userId.setError("address tidak boleh kosong");
            progressDialog.dismiss();
            return;
        }

        DoctorDataRequest request = new DoctorDataRequest();
        request.setDokterId(userId.getText().toString().trim());
        request.setFirstName(firstName.getText().toString().trim());
        request.setLastName(lastName.getText().toString().trim());
        request.setAddress(address.getText().toString().trim());
        request.setSpecialization(specializationCode);

        if (isEdit){
            DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
            Call<ResponseBody> callDataApi = dataApi.updateDokter(doctorList.get(0).getId(), token, request);
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
            Call<ResponseBody> callRegisterApi = registerApi.setDokter(token, request);
            callRegisterApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        JSONObject object = new JSONObject(response.body().string());

                        if (object.getInt("code") == 2){
                            progressDialog.dismiss();
                            JSONObject jsonObject = object.getJSONObject("data");

                            DoctorDataResponse dataResponse = new DoctorDataResponse();
                            dataResponse.setCreatedDate(jsonObject.getString("createdDate"));
                            dataResponse.setDokterId(jsonObject.getString("dokterId"));
                            dataResponse.setPassword(jsonObject.getString("password"));
                            dataResponse.setName(jsonObject.getString("name"));
                            dataResponse.setSpecialization(jsonObject.getString("specialization"));
                            dataResponse.setMessage(object.getString("info"));

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

    private void sendResponseToAnotherFragment(DoctorDataResponse response){
        Bundle bundle = new Bundle();
        ArrayList<DoctorDataResponse> doctorResponseList = new ArrayList<>();
        doctorResponseList.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), doctorResponseList);

        DoctorInputResultFragment fragment = new DoctorInputResultFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "doctor_register_result_fragment")
                .addToBackStack(null)
                .commit();
    }

}
