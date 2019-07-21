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
import id.developer.rs_thamrin.adapter.AdminListAdapter;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.AdminData;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDataListFragment extends Fragment implements AdminListAdapter.Listener{
    private RecyclerView recyclerView;
    private AdminListAdapter adapter;
    private List<AdminData> adminList;
    private ProgressBar progressBar;
    private FloatingActionButton addAdmin;

    public AdminDataListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_admin_data_list, container, false);

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Admin");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);
        getListAdmin();

        addAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("isEdit", false);

                AdminDataInputFragment fragment = new AdminDataInputFragment();
                fragment.setArguments(bundle);

                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, fragment, "admin_data_input_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }

    private void bindView(View view){
        addAdmin = view.findViewById(R.id.add_admin);
        adapter = new AdminListAdapter(getActivity(), this);

        progressBar = (ProgressBar)view.findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#455ede"),android.graphics.PorterDuff.Mode.MULTIPLY);

        recyclerView =  (RecyclerView)view.findViewById(R.id.admin_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getListAdmin(){
        progressBar.setVisibility(View.VISIBLE);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.getAdmin();
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressBar.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        adminList = new ArrayList<>();

                        for (int i= 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            AdminData admin = new AdminData();
                            admin.setId(jsonObject.getInt("id"));
                            admin.setUserId(jsonObject.getString("userId"));
                            admin.setName(jsonObject.getString("name"));
                            admin.setFirstName(jsonObject.getString("firstName"));
                            admin.setLastName(jsonObject.getString("lastName"));
                            admin.setBirthDate( jsonObject.getString("birthDate"));
                            admin.setBirthPlace(jsonObject.getString("birthPlace"));
                            admin.setUserRole(jsonObject.getString("userRole"));
                            admin.setUserStatus(jsonObject.getString("userStatus"));
                            admin.setAddress(jsonObject.getString("address"));

                            adminList.add(admin);
                        }

                        adapter.setData(adminList);
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
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onClick(AdminData dataPositions) {
        Bundle bundle = new Bundle();
        ArrayList<AdminData> list = new ArrayList<>();
        list.add(dataPositions);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), list);

        AdminDataDetailFragment fragment = new AdminDataDetailFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "NewFragmentTag")
                .addToBackStack(null)
                .commit();
    }
}
