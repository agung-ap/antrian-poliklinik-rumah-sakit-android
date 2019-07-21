package id.developer.rs_thamrin.Fragment.admin;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.util.Log;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import id.developer.rs_thamrin.Fragment.LoginFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.AdminData;
import id.developer.rs_thamrin.model.DoctorData;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AdminDataDetailFragment extends Fragment {
    private static final String TAG = AdminDataDetailFragment.class.getName();
    private SharedPreferences preferences;
    private String token, userRole;

    private ArrayList<AdminData> adminDataList = new ArrayList<>();
    private TextView name ;
    private TextView birthPlace;
    private TextView birthDate;
    private TextView address;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");
        adminDataList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_data_detail, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Detail");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        Log.i(TAG, "admin list data : " + adminDataList.get(0).getName());
        bindView(view);
        setData();

        return view;
    }

    private void bindView(View view){
        name = (TextView)view.findViewById(R.id.admin_name_detail);
        birthPlace = (TextView)view.findViewById(R.id.birth_place_detail);
        birthDate = (TextView)view.findViewById(R.id.birth_date_detail);
        address = (TextView)view.findViewById(R.id.address_detail);
    }

    private void setData(){
        try {
            Date d = new SimpleDateFormat("yyyy-MM-dd").parse(adminDataList.get(0).getBirthDate());
            birthDate.setText(new SimpleDateFormat("dd MMM yyyy").format(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        name.setText(adminDataList.get(0).getName());
        birthPlace.setText(adminDataList.get(0).getBirthPlace());
        address.setText(adminDataList.get(0).getAddress());
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
                sendResponseToAnotherFragment(adminDataList);
                break;
            case R.id.delete_menu :
                String info = "Apakah anda yakin akan menghapus data Admin " + adminDataList.get(0).getName();
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage(info);

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dokterAdmin(adminDataList.get(0).getId());
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

    private void dokterAdmin(long id){
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Menghapus data Admin " + adminDataList.get(0).getName());
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callDataApi = dataApi.deleteAdmin(id, token);
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

    private void sendResponseToAnotherFragment(ArrayList<AdminData> response){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", true);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), response);

        AdminDataInputFragment fragment = new AdminDataInputFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "admin_edit_result_fragment")
                .addToBackStack(null)
                .commit();
    }
}
