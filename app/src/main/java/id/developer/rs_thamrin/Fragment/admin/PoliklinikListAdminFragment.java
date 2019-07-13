package id.developer.rs_thamrin.Fragment.admin;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.Fragment.HomeFragment;
import id.developer.rs_thamrin.Fragment.user.PoliklinikDetailFragment;
import id.developer.rs_thamrin.Fragment.user.PoliklinikListFragment;
import id.developer.rs_thamrin.Fragment.user.PoliklinikRegisterFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.adapter.PoliklinikListAdapter;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.Poliklinik;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PoliklinikListAdminFragment extends Fragment implements PoliklinikListAdapter.Listener{
    private RecyclerView recyclerView;
    private PoliklinikListAdapter adapter;
    private List<Poliklinik> poliklinikList;
    private ProgressBar progressBar;
    private FloatingActionButton addPoliklinik;

    public PoliklinikListAdminFragment() {
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
        View view = inflater.inflate(R.layout.fragment_poliklinik_list_admin, container, false);

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Poliklinik");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);

        getPoliklinikList();


        addPoliklinik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", false);

                PoliklinikInputFragment fragment = new PoliklinikInputFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, fragment, "poliklinik_input_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void bindView(View view){
        addPoliklinik = view.findViewById(R.id.add_poliklinik);
        adapter = new PoliklinikListAdapter(getActivity(), this);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#455ede"),android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerView =  (RecyclerView)view.findViewById(R.id.poliklinik_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getPoliklinikList(){
        progressBar.setVisibility(View.VISIBLE);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getPoliklinik();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressBar.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        poliklinikList = new ArrayList<>();

                        for (int i= 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            Poliklinik poliklinik = new Poliklinik();
                            poliklinik.setId(jsonObject.getInt("id"));
                            poliklinik.setCode(jsonObject.getString("code"));
                            poliklinik.setDoctorId(jsonObject.getInt("doctorId"));
                            poliklinik.setScheduleId(jsonObject.getInt("scheduleId"));
                            poliklinik.setPoliklinikNameId(jsonObject.getInt("specializationId"));
                            poliklinik.setSchedule(jsonObject.getString("schedule"));
                            poliklinik.setTime(jsonObject.getString("time"));
                            poliklinik.setDoctorName(jsonObject.getString("doctorName"));
                            poliklinik.setPoliklinikName(jsonObject.getString("poliklinikName"));
                            poliklinik.setKuota(jsonObject.getInt("kuota"));

                            poliklinikList.add(poliklinik);
                        }

                        adapter.setData(poliklinikList);
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
    public void onClick(Poliklinik dataPosition) {
        Bundle bundle = new Bundle();
        ArrayList<Poliklinik> polikliniks = new ArrayList<>();
        polikliniks.add(dataPosition);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), polikliniks);

        PoliklinikDetailFragment fragment = new PoliklinikDetailFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "NewFragmentTag")
                .addToBackStack(null)
                .commit();
    }



//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()){
//            case android.R.id.home:
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.fragment_layout_home, new HomeFragment(), "fragment_home")
//                        .commit();
//                break;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
