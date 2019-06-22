package id.developer.rs_thamrin.Fragment.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.adapter.DoctorListAdapter;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.master.DoctorData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterDataListDoctorFragment extends Fragment implements DoctorListAdapter.Listener{

    private RecyclerView recyclerView;
    private DoctorListAdapter adapter;
    private List<DoctorData> doctorDataList;
    private ProgressBar progressBar;

    private FloatingActionButton addDoctor;

    public MasterDataListDoctorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_master_data_list_doctor, container, false);
        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Dokter");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);
        getDoctorList();

        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, new MasterDataDoctorInputFragment(), "doctor_input_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    private void bindView(View view){
        addDoctor = view.findViewById(R.id.add_doctor);
        adapter = new DoctorListAdapter(getActivity(), this);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#455ede"),android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerView = view.findViewById(R.id.doctor_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getDoctorList(){
        progressBar.setVisibility(View.VISIBLE);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getDokter();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressBar.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        doctorDataList = new ArrayList<>();

                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            DoctorData doctorData = new DoctorData();
                            doctorData.setId(jsonObject.getInt("id"));
                            doctorData.setName(jsonObject.getString("name"));
                            doctorData.setSpecialization(jsonObject.getString("specialization"));
                            doctorData.setSpecializationCode(jsonObject.getString("specializationCode"));
                            doctorData.setAddress(jsonObject.getString("address"));

                            doctorDataList.add(doctorData);
                        }

                        adapter.setData(doctorDataList);
                    }else {
                        progressBar.setVisibility(View.GONE);
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


    @Override
    public void onClick(DoctorData dataPosition) {
        Bundle bundle = new Bundle();
        ArrayList<DoctorData> doctorData = new ArrayList<>();
        doctorData.add(dataPosition);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), doctorData);

        DoctorDataDetailFragment fragment = new DoctorDataDetailFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "doctor_data_detail_fragment")
                .addToBackStack(null)
                .commit();
    }
}
