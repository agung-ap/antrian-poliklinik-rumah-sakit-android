package id.developer.rs_thamrin.Fragment.user;

import android.graphics.Color;
import android.os.Bundle;
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
import id.developer.rs_thamrin.adapter.PoliklinikListAdapter;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.Poliklinik;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PoliklinikListFragment extends Fragment implements PoliklinikListAdapter.Listener{
    private RecyclerView recyclerView;
    private PoliklinikListAdapter adapter;
    private List<Poliklinik> poliklinikList;
    private ProgressBar progressBar;

    public PoliklinikListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_poliklinik_list, container, false);

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Poliklinik");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);

        getPoliklinikList();

        return view;
    }

    private void bindView(View view){
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
}
