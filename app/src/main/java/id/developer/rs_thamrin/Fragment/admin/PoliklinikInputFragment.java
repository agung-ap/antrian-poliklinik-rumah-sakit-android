package id.developer.rs_thamrin.Fragment.admin;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.ScrollingTabContainerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.Fragment.HomeFragment;
import id.developer.rs_thamrin.Fragment.user.PoliklinikListFragment;
import id.developer.rs_thamrin.Fragment.user.PoliklinikRegisterFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.Poliklinik;
import id.developer.rs_thamrin.model.master.DoctorData;
import id.developer.rs_thamrin.model.master.TypeOfSchedule;
import id.developer.rs_thamrin.model.master.TypeOfSpecialization;
import id.developer.rs_thamrin.model.request.PoliklinikRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.support.constraint.Constraints.TAG;


public class PoliklinikInputFragment extends Fragment {
    private SharedPreferences preferences;
    private String token, userRole;

    private int doctorId;
    private String specializationCode;
    private String scheduleCode;

    private boolean isEdit = false;
    private List<Poliklinik> poliklinikList;

    private AppCompatSpinner scheduleSpinner;
    private AppCompatSpinner doctorSpinner;
    private AppCompatSpinner specializationSpinner;
    private EditText kuota;
    private Button savePoliklinik;

    private List<String> scheduleName;
    private List<TypeOfSchedule> scheduleList;
    private List<String> specializationName;
    private List<TypeOfSpecialization> specializationList;
    private List<String> doctorDataName;
    private List<DoctorData> doctorDataList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");

        isEdit = getArguments().getBoolean("isEdit", false);
        poliklinikList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View view = inflater.inflate(R.layout.fragment_poliklinik_input, container, false);
            if (isEdit){
                ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Edit Poliklinik");
                ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }else {
                ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Input Poliklinik");
                ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }

            bindView(view);
            getDoctor();
            getSchedule();
            getSpecialization();

            if (isEdit){
                kuota.setText(String.valueOf(poliklinikList.get(0).getKuota()));
            }

            savePoliklinik.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PoliklinikRequest request = new PoliklinikRequest();
                    request.setDoctorId(doctorId);
                    if (kuota.getText().toString().trim().equals("")) {
                        Toast.makeText(getActivity(), "kuota belum diisi", Toast.LENGTH_SHORT).show();
                    }else {
                        request.setKuota(Integer.parseInt(kuota.getText().toString().trim()));
                    }
                    request.setSchedule(scheduleCode);
                    request.setSpecialization(specializationCode);

                    if (isEdit){
                        handlePoliklinikEdit(request);
                    }else {
                        handlePoliklinikSave(request);
                    }
                }
            });

            return view;
    }

    private void bindView (View view){
        scheduleSpinner = view.findViewById(R.id.schedule);
        doctorSpinner = view.findViewById(R.id.doctor);
        specializationSpinner = view.findViewById(R.id.specialization);
        kuota = view.findViewById(R.id.kuota_poliklinik);
        savePoliklinik = view.findViewById(R.id.poliklinik_input_save);
    }

    private void handlePoliklinikEdit(PoliklinikRequest request){
        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.updatePoliklinik(poliklinikList.get(0).getId(),token,request);
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 2){
                        Toast.makeText(getActivity(), object.getString("info"), Toast.LENGTH_SHORT).show();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();

                        if (manager.getBackStackEntryCount() > 0) {
                            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        trans.remove(new PoliklinikInputFragment());
                        trans.commit();
                    }else {
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

            }
        });
    }

    private void handlePoliklinikSave(PoliklinikRequest request){
        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.setPoliklinik(token,request);
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 2){
                        Toast.makeText(getActivity(), object.getString("info"), Toast.LENGTH_SHORT).show();

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();

                        if (manager.getBackStackEntryCount() > 0) {
                            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        trans.remove(new PoliklinikInputFragment());
                        trans.commit();
                    }else {
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

            }
        });
    }

    private void getSchedule(){
        scheduleName = new ArrayList<>();
        scheduleList = new ArrayList<>();

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getSchedule();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        JSONArray jsonArray = object.getJSONArray("data");

                        if (!isEdit){
                            scheduleList.add(null);
                            scheduleName.add("");
                        }else {
                            scheduleName.add("");
                        }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            TypeOfSchedule schedule = new TypeOfSchedule();
                            schedule.setName(jsonObject.getString("name"));
                            schedule.setTime(jsonObject.getString("time"));

                            scheduleName.add(schedule.getName() + " : "+ schedule.getTime());
                            scheduleList.add(schedule);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                scheduleName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        scheduleSpinner.setAdapter(adapter);
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

        scheduleSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit){
                    if (position == 0){
                        scheduleSpinner.setSelection(poliklinikList.get(0).getScheduleId());
                        scheduleCode = scheduleList.get(position).getName();
                    }else {
                        scheduleSpinner.setSelection(position, true);
                        scheduleCode = scheduleList.get(position - 1).getName();
                    }
                }else {
                    if (position == 0){
                        scheduleSpinner.setSelection(position, true);
                        scheduleCode = "";
                    }else {
                        scheduleCode = scheduleList.get(position).getName();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void getDoctor(){
        doctorDataName = new ArrayList<>();
        doctorDataList = new ArrayList<>();

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getDokter();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        JSONArray jsonArray = object.getJSONArray("data");

                        if (!isEdit){
                            doctorDataList.add(null);
                            doctorDataName.add("");
                        }else {
                            doctorDataName.add("");
                        }

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            DoctorData doctorData = new DoctorData();
                            doctorData.setId(jsonObject.getInt("id"));
                            doctorData.setName(jsonObject.getString("name"));
                            doctorData.setSpecialization(jsonObject.getString("specialization"));
                            doctorData.setSpecializationCode(jsonObject.getString("specializationCode"));
                            doctorData.setAddress(jsonObject.getString("address"));

                            doctorDataName.add(doctorData.getName() + " : "+ doctorData.getSpecialization());
                            doctorDataList.add(doctorData);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                doctorDataName);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        doctorSpinner.setAdapter(adapter);

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

        if (isEdit){
            doctorSpinner.setSelection(poliklinikList.get(0).getDoctorId() - 1);
        }
        doctorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit){
                    if (position == 0){
                        doctorSpinner.setSelection(poliklinikList.get(0).getDoctorId());
                        doctorId = doctorDataList.get(position).getId();
                    }else {
                        doctorSpinner.setSelection(position, true);
                        doctorId = doctorDataList.get(position - 1).getId();
                    }
                }else {
                    if (position == 0){
                        doctorSpinner.setSelection(position, true);
                        doctorId = 0;
                    }else {
                        doctorId = doctorDataList.get(position).getId();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

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
                            specialization.setId(jsonObject.getInt("id"));
                            specialization.setName(jsonObject.getString("name"));
                            specialization.setCode(jsonObject.getString("code"));

                            specializationName.add(specialization.getName());
                            specializationList.add(specialization);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                                getActivity(),android.R.layout.simple_spinner_item,
                                specializationName){
                        };
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        specializationSpinner.setAdapter(adapter);
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


        specializationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (isEdit){
                    if (position == 0){
                        specializationSpinner.setSelection(poliklinikList.get(0).getPoliklinikNameId());
                        specializationCode = specializationList.get(position).getCode();
                    }else {
                        specializationSpinner.setSelection(position, true);
                        specializationCode = specializationList.get(position - 1).getCode();
                    }
                }else {
                    if (position == 0){
                        specializationSpinner.setSelection(position, true);
                        specializationCode = "";
                    }else {
                        specializationCode = specializationList.get(position).getCode();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                if (isEdit){
                    specializationSpinner.setSelection(poliklinikList.get(0).getPoliklinikNameId() - 1);
                }
            }
        });
    }

}
