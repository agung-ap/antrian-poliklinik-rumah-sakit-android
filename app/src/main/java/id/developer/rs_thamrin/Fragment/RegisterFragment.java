package id.developer.rs_thamrin.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
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
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import id.developer.rs_thamrin.Fragment.admin.UserApprovalResultFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.MainActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RegisterApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.master.District;
import id.developer.rs_thamrin.model.master.Education;
import id.developer.rs_thamrin.model.master.Family;
import id.developer.rs_thamrin.model.master.Job;
import id.developer.rs_thamrin.model.master.MariedStatus;
import id.developer.rs_thamrin.model.master.Province;
import id.developer.rs_thamrin.model.master.Regency;
import id.developer.rs_thamrin.model.master.TypeOfAddress;
import id.developer.rs_thamrin.model.master.TypeOfIdentityCard;
import id.developer.rs_thamrin.model.master.TypeOfPayment;
import id.developer.rs_thamrin.model.master.Village;
import id.developer.rs_thamrin.model.request.CustomerRegisterRequest;
import id.developer.rs_thamrin.model.response.CustomerRegisterResponse;
import id.developer.rs_thamrin.model.response.UserApprovalResponse;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;

public class RegisterFragment extends Fragment{
    private EditText firstName;
    private EditText lastName;
    private EditText birthPlace;
    private EditText birthDate;
    private EditText family;
    private AppCompatSpinner typeOfFamily;
    private EditText identityCard;
    private AppCompatSpinner typeOfIdentityCard;
    private AppCompatSpinner gender;
    private AppCompatSpinner mariedStatus;
    private AppCompatSpinner education;
    private EditText phoneNumber;
    private AppCompatSpinner job;
    private AppCompatSpinner typeOfPayment;
    private EditText country;
    private AppCompatSpinner province;
    private AppCompatSpinner regency;
    private AppCompatSpinner district;
    private AppCompatSpinner village;
    private EditText address;
    private AppCompatSpinner typeOfAddress;

    private Button register;

    private String [] genderList = {"MALE", "FEMALE"};

    private List<String> familyTypeName = new ArrayList<>();
    private List<String> identityCardTypeName = new ArrayList<>();
    private List<String> mariedStatusName = new ArrayList<>();
    private List<String> educationName = new ArrayList<>();
    private List<String> jobName = new ArrayList<>();
    private List<String> genderName = new ArrayList<>();
    private List<String> paymentTypeName = new ArrayList<>();
    private List<String> provinceName = new ArrayList<>();
    private List<String> regencyName = new ArrayList<>();
    private List<String> districtName = new ArrayList<>();
    private List<String> villageName = new ArrayList<>();
    private List<String> addressTypeName = new ArrayList<>();

    private List<Family> familyTypeList = new ArrayList<>();
    private List<TypeOfIdentityCard> identityCardTypelist = new ArrayList<>();
    private List<MariedStatus> mariedStatusList = new ArrayList<>();
    private List<Education> educationList = new ArrayList<>();
    private List<Job> jobList = new ArrayList<>();
    private List<TypeOfPayment> paymentTypeList = new ArrayList<>();
    private List<Province> provinceList = new ArrayList<>();
    private List<Regency> regencyList = new ArrayList<>();
    private List<District> districtList = new ArrayList<>();
    private List<Village> villageList = new ArrayList<>();
    private List<TypeOfAddress> addressTypeList = new ArrayList<>();

    private String familyCode;
    private String identitycardCode;
    private String mariedStatusCode;
    private String educationCode;
    private String jobCode;
    private String typeOfPaymentCode;
    private String genderCode;
    private long provinceCode;
    private long regencyCode;
    private long districtCode;
    private long villageCode;
    private String typeOfAddressCode;

    private Calendar myCalendar = Calendar.getInstance();
    private ProgressDialog progressDialog;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindView(view);

        loadSpinnerData();
        spinnerSelected();

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegister();
            }
        });

        return view;
    }

    private void bindView(View view) {
        firstName = view.findViewById(R.id.first_name);
        lastName = view.findViewById(R.id.last_name);
        birthPlace = view.findViewById(R.id.birth_place);
        birthDate = view.findViewById(R.id.birth_date);
        family = view.findViewById(R.id.family_name);
        typeOfFamily = view.findViewById(R.id.type_of_family_name);
        identityCard = view.findViewById(R.id.identity_card);
        typeOfIdentityCard = view.findViewById(R.id.type_of_identity_card);
        gender =  view.findViewById(R.id.gender);
        mariedStatus = view.findViewById(R.id.maried_status);
        education = view.findViewById(R.id.education);
        phoneNumber = view.findViewById(R.id.phone_number);
        job = view.findViewById(R.id.job);
        typeOfPayment = view.findViewById(R.id.type_of_payment);
//        country = (EditText)view.findViewById(R.id.country);
        province = view.findViewById(R.id.province);
        regency = view.findViewById(R.id.regency);
        district = view.findViewById(R.id.district);
        village = view.findViewById(R.id.village);
        address = view.findViewById(R.id.address);
        typeOfAddress = view.findViewById(R.id.type_of_address);

        register = view.findViewById(R.id.registrasi);
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

    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void spinnerSelected(){
        typeOfFamily.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                familyCode = familyTypeList.get(position).getCode();
                Log.i(TAG, "familyCode: " + familyCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeOfIdentityCard.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                identitycardCode = identityCardTypelist.get(position).getCode();
                Log.i(TAG, "identitycardCode: " + identitycardCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        mariedStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mariedStatusCode = mariedStatusList.get(position).getCode();
                Log.i(TAG, "mariedStatusCode: " + mariedStatusCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        education.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                educationCode = educationList.get(position).getCode();
                Log.i(TAG, "educationCode: " + educationCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        job.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                jobCode = jobList.get(position).getCode();
                Log.i(TAG, "jobCode: " + jobCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeOfPayment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfPaymentCode = paymentTypeList.get(position).getCode();
                Log.i(TAG, "typeOfPaymentCode: " + typeOfPaymentCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        typeOfAddress.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                typeOfAddressCode = addressTypeList.get(position).getCode();
                Log.i(TAG, "typeOfAddressCode: " + typeOfAddressCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                genderCode = genderName.get(position);
                Log.i(TAG, "genderCode: " + genderCode);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void loadSpinnerData(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        //gender spinner
        genderName.addAll(Arrays.asList(genderList));
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(),android.R.layout.simple_spinner_item,
                genderName);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gender.setAdapter(adapter);

        //family spinner
        dataApi.getFamily().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Family family = new Family();
                            family.setCode(jsonObject.getString("code"));
                            family.setName(jsonObject.getString("name"));

                            familyTypeName.add(jsonObject.getString("name"));
                            familyTypeList.add(family);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                familyTypeName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        typeOfFamily.setAdapter(adapter);

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

        dataApi.getIndentityCard().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfIdentityCard identityCard = new TypeOfIdentityCard();
                            identityCard.setCode(jsonObject.getString("code"));
                            identityCard.setName(jsonObject.getString("name"));

                            identityCardTypeName.add(jsonObject.getString("name"));
                            identityCardTypelist.add(identityCard);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                identityCardTypeName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        typeOfIdentityCard.setAdapter(adapter);

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

        dataApi.getMariedStatus().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            MariedStatus mariedStatus = new MariedStatus();
                            mariedStatus.setCode(jsonObject.getString("code"));
                            mariedStatus.setName(jsonObject.getString("name"));

                            mariedStatusName.add(jsonObject.getString("name"));
                            mariedStatusList.add(mariedStatus);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                mariedStatusName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        mariedStatus.setAdapter(adapter);

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

        dataApi.getEducation().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Education education = new Education();
                            education.setCode(jsonObject.getString("code"));
                            education.setName(jsonObject.getString("name"));

                            educationName.add(jsonObject.getString("name"));
                            educationList.add(education);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                educationName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        education.setAdapter(adapter);

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

        dataApi.getJob().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Job job = new Job();
                            job.setCode(jsonObject.getString("code"));
                            job.setName(jsonObject.getString("name"));

                            jobName.add(jsonObject.getString("name"));
                            jobList.add(job);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                jobName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        job.setAdapter(adapter);

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

        dataApi.getPayment().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfPayment typeOfPayment = new TypeOfPayment();
                            typeOfPayment.setCode(jsonObject.getString("code"));
                            typeOfPayment.setName(jsonObject.getString("name"));

                            paymentTypeName.add(jsonObject.getString("name"));
                            paymentTypeList.add(typeOfPayment);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                paymentTypeName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        typeOfPayment.setAdapter(adapter);

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

        dataApi.getAddress().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfAddress typeOfAddress = new TypeOfAddress();
                            typeOfAddress.setCode(jsonObject.getString("code"));
                            typeOfAddress.setName(jsonObject.getString("name"));

                            addressTypeName.add(jsonObject.getString("name"));
                            addressTypeList.add(typeOfAddress);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                addressTypeName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        typeOfAddress.setAdapter(adapter);

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

        dataApi.getProvince().enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            Province province = new Province();
                            province.setId(jsonObject.getLong("id"));
                            province.setName(jsonObject.getString("provinceName"));

                            provinceName.add(jsonObject.getString("provinceName"));
                            provinceList.add(province);

                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                provinceName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        province.setAdapter(adapter);
                        adapter.notifyDataSetChanged();


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

        //set item click listenerc
        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                provinceCode = provinceList.get(position).getId();
                Log.i(TAG, "provinceCode: " + provinceCode);

                getRegencyData(provinceList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getRegencyData(long regencyId){

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        dataApi.getRegency(regencyId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        if (regencyList.size() > 0 && regencyName.size() > 0){
                            regencyList.clear();
                            regencyName.clear();

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Regency regency = new Regency();
                                regency.setId(jsonObject.getLong("id"));
                                regency.setName(jsonObject.getString("regencyName"));

                                regencyName.add(jsonObject.getString("regencyName"));
                                regencyList.add(regency);
                            }
                        }else {
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Regency regency = new Regency();
                                regency.setId(jsonObject.getLong("id"));
                                regency.setName(jsonObject.getString("regencyName"));

                                regencyName.add(jsonObject.getString("regencyName"));
                                regencyList.add(regency);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                regencyName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        regency.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

        regency.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                regencyCode = regencyList.get(position).getId();
                Log.i(TAG, "regencyCode: " + regencyCode);

                getDistrictData(regencyList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getDistrictData(long id){

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        dataApi.getDistrict(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {

                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0) {
                        JSONArray jsonArray = object.getJSONArray("data");

                        if (districtList.size() > 0 && districtName.size() > 0){
                            districtList.clear();
                            districtName.clear();

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                District district = new District();
                                district.setId(jsonObject.getLong("id"));
                                district.setName(jsonObject.getString("districtName"));

                                districtName.add(jsonObject.getString("districtName"));
                                districtList.add(district);
                            }
                        }else {
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                District district = new District();
                                district.setId(jsonObject.getLong("id"));
                                district.setName(jsonObject.getString("districtName"));

                                districtName.add(jsonObject.getString("districtName"));
                                districtList.add(district);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                districtName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        district.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
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

        district.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                districtCode = districtList.get(position).getId();
                Log.i(TAG, "districtCode: " + districtCode);

                getVillageData(districtList.get(position).getId());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getVillageData(long id){
        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        dataApi.getVillage(id).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 0) {
                        progressDialog.dismiss();
                        JSONArray jsonArray = object.getJSONArray("data");

                        if (villageList.size() > 0 && villageName.size() > 0){
                            villageName.clear();
                            villageList.clear();

                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Village village = new Village();
                                village.setId(jsonObject.getLong("id"));
                                village.setName(jsonObject.getString("villageName"));

                                villageName.add(jsonObject.getString("villageName"));
                                villageList.add(village);
                            }
                        }else {
                            for (int i = 0; i < jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);

                                Village village = new Village();
                                village.setId(jsonObject.getLong("id"));
                                village.setName(jsonObject.getString("villageName"));

                                villageName.add(jsonObject.getString("villageName"));
                                villageList.add(village);
                            }
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                villageName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        village.setAdapter(adapter);
                        adapter.notifyDataSetChanged();

                        village.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                villageCode = villageList.get(position).getId();
                                Log.i(TAG, "villageCode: " + villageCode);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
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

    private void handleRegister(){
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        CustomerRegisterRequest req = new CustomerRegisterRequest();
        req.setFirstName(firstName.getText().toString().trim());
        req.setLastName(lastName.getText().toString().trim());
        req.setBirthPlace(birthPlace.getText().toString().trim());
        req.setBirthDate(birthDate.getText().toString().trim());
        req.setIdentityCard(identityCard.getText().toString().trim());
        req.setTypeOfIdentityCard(identitycardCode);
        req.setFamilyName(family.getText().toString().trim());
        req.setTypeFamilyName(familyCode);
        req.setGender(genderCode);
        req.setMariedStatus(mariedStatusCode);
        req.setEducation(educationCode);
        req.setPhoneNumber(phoneNumber.getText().toString().trim());
        req.setJob(jobCode);
        req.setTypeOfPayment(typeOfPaymentCode);
        req.setImageIdentityCard("");
        req.setCountry("INDONESIA");
        req.setProvince(provinceCode);
        req.setRegencie(regencyCode);
        req.setDistrict(districtCode);
        req.setVillage(BigDecimal.valueOf(villageCode));
        req.setAddress(address.getText().toString().trim());
        req.setTypeOfAddress(typeOfAddressCode);

        RegisterApi registerApi = RetrofitBuilder.getApiService().create(RegisterApi.class);
        Call<ResponseBody> callRegisterApi = registerApi.setCustomer(req);
        callRegisterApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 2){
                        progressDialog.dismiss();
                        JSONObject jsonObject = object.getJSONObject("data");

                        CustomerRegisterResponse resp = new CustomerRegisterResponse();
                        resp.setInfo(object.getString("info"));
                        resp.setName(jsonObject.getString("name"));
                        resp.setRmId(jsonObject.getString("rmId"));
                        resp.setPassword(jsonObject.getString("password"));
                        resp.setCreatedDate(jsonObject.getString("createdDate"));

                        sendResponseToAnotherFragment(resp);
                    }else {
                        progressDialog.dismiss();
                        GlobalFunction.showMessage(getActivity(), object.getString("info"));
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

    private void sendResponseToAnotherFragment(CustomerRegisterResponse response){
        Bundle bundle = new Bundle();
        ArrayList<CustomerRegisterResponse> customerRegisterResponses = new ArrayList<>();
        customerRegisterResponses.add(response);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), customerRegisterResponses);

        RegisterFragmentResult fragment = new RegisterFragmentResult();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout, fragment, "register_fragment_result")
                .commit();
    }

}
