package id.developer.rs_thamrin.Fragment.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.DoctorData;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorDataDetailFragment extends Fragment {
    private SharedPreferences preferences;
    private String token, userRole;

    private ArrayList<DoctorData> doctorDataList = new ArrayList<>();
    private TextView name ;
    private TextView specialization;
    private TextView address;

    public DoctorDataDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");
        doctorDataList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_data_detail, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Detail");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        bindView(view);
        setData();

        return view;
    }

    private void bindView(View view){
        name = (TextView)view.findViewById(R.id.doctor_name_detail);
        specialization = (TextView)view.findViewById(R.id.doctor_specialization_detail);
        address = (TextView)view.findViewById(R.id.doctor_address_detail);
    }

    private void setData(){
        name.setText(doctorDataList.get(0).getName());
        specialization.setText(doctorDataList.get(0).getSpecialization());
        address.setText(doctorDataList.get(0).getAddress());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null){
            menu.clear();
        }

        inflater.inflate(R.menu.list_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu :
                sendResponseToAnotherFragment(doctorDataList);
                break;
            case R.id.delete_menu :
                String info = "Apakah anda yakin akan menghapus data Dokter " + doctorDataList.get(0).getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(info);

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dokterDelete(doctorDataList.get(0).getId());
                    }
                });

                builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.create().show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void dokterDelete(long id){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Menghapus data Dokter " + doctorDataList.get(0).getName());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.deleteDokter(id, token);
        callDataApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressDialog.dismiss();
                        GlobalFunction.toast(getActivity(), object.getString("info"));

                        FragmentManager manager = getActivity().getSupportFragmentManager();
                        FragmentTransaction trans = manager.beginTransaction();

                        if (manager.getBackStackEntryCount() > 0) {
                            FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                            manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                        }

                        trans.remove(new DoctorDataDetailFragment());
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
    }

    private void sendResponseToAnotherFragment(ArrayList<DoctorData> response){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", true);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), response);

        MasterDataDoctorInputFragment fragment = new MasterDataDoctorInputFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "dokter_edit_result_fragment")
                .addToBackStack(null)
                .commit();
    }
}
