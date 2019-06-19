package id.developer.rs_thamrin.Fragment.admin;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RegisterApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.master.DoctorData;
import id.developer.rs_thamrin.model.master.TypeOfSpecialization;
import id.developer.rs_thamrin.model.request.DoctorDataRequest;
import id.developer.rs_thamrin.model.response.DoctorDataResponse;
import id.developer.rs_thamrin.model.response.UserApprovalResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataDoctorInputFragment extends Fragment {
    private SharedPreferences preferences;
    private String userRole, token;
    private String specializationCode;

    private EditText userId;
    private EditText firstName;
    private EditText lastName;
    private EditText birthPlace;
    private EditText birthDate;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master_data_doctor_input, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Input Dokter");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);
        getSpecialization();

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDoctorRegister();
            }
        });


        return view;
    }

    final DatePickerDialog.OnDateSetListener mdate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthDate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void bindView(View view) {
        userId = view.findViewById(R.id.user_id_dokter);
        firstName = view.findViewById(R.id.first_name_dokter);
        lastName = view.findViewById(R.id.last_name_dokter);
        birthPlace = view.findViewById(R.id.birth_place_dokter);
        birthDate = view.findViewById(R.id.birth_date_dokter);
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

                specializationCode = specializationList.get(position).getCode();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void handleDoctorRegister(){
        DoctorDataRequest request = new DoctorDataRequest();
        request.setDokterId(userId.getText().toString().trim());
        request.setFirstName(firstName.getText().toString().trim());
        request.setLastName(lastName.getText().toString().trim());
        request.setBirthPlace(birthPlace.getText().toString().trim());
        request.setBirthDate(birthDate.getText().toString().trim());
        request.setAddress(address.getText().toString().trim());
        request.setSpecialization(specializationCode);

        RegisterApi registerApi = RetrofitBuilder.getApiService().create(RegisterApi.class);
        Call<ResponseBody> callRegisterApi = registerApi.setDokter(token, request);
        callRegisterApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 2){
                       JSONObject jsonObject = object.getJSONObject("data");

                        DoctorDataResponse dataResponse = new DoctorDataResponse();
                        dataResponse.setCreatedDate(jsonObject.getString("createdDate"));
                        dataResponse.setDokterId(jsonObject.getString("dokterId"));
                        dataResponse.setPassword(jsonObject.getString("password"));
                        dataResponse.setName(jsonObject.getString("name"));
                        dataResponse.setSpecialization(jsonObject.getString("specialization"));
                        dataResponse.setMessage(object.getString("info"));

                        sendResponseToAnotherFragment(dataResponse);
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

    private void sendResponseToAnotherFragment(DoctorDataResponse response){
        Bundle bundle = new Bundle();
        ArrayList<DoctorDataResponse> doctorResponseList = new ArrayList<>();
        doctorResponseList.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), doctorResponseList);

        DoctorInputResultFragment fragment = new DoctorInputResultFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "doctor_register_result_fragment")
                .commit();
    }

}
