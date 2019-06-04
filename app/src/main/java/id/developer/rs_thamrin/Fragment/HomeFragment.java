package id.developer.rs_thamrin.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.activity.MainActivity;
import id.developer.rs_thamrin.adapter.HomeAdapter;
import id.developer.rs_thamrin.api.LoginApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.MenuItemModel;
import id.developer.rs_thamrin.util.GlobalFunction;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class HomeFragment extends Fragment {
    private static final String TAG = HomeFragment.class.getName();

    private SharedPreferences preferences;
    private String userRole;
    private String token;

    private GridView homeMenu;
    private ArrayList<MenuItemModel> menuitem;
    private HomeAdapter adapter;
    private MenuItemModel item;

    private ProgressDialog progressDialog;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        bindView(view);

        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Home");

        if (userRole.equals("default")){
            Toast.makeText(getActivity(), "Silahkan Login terlebih dahulu", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        if (userRole.equals("USER")){
            adapter.clear();
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getContext(),menuItem());
            homeMenu.setAdapter(adapter);

            homeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            Log.i(TAG, "userRole: " + userRole);
                            break;
                        case 1:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 2:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 3:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                    }
                }
            });
        }
        if (userRole.equals("ADMIN")){
            adapter.clear();
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getContext(),menuItem());
            homeMenu.setAdapter(adapter);

            homeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 1:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 2:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 3:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                    }
                }
            });
        }
        if (userRole.equals("DOKTER")){
//            adapter.clear();
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getContext(),menuItem());
            homeMenu.setAdapter(adapter);

            homeMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    switch (position){
                        case 0:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 1:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 2:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                        case 3:
                            Log.i(TAG, "userRole: " + userRole);

                            break;
                    }
                }
            });

        }
        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu :
                logout(token);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout(String token) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Logout");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(false);

        LoginApi loginApi = RetrofitBuilder.getApiService().create(LoginApi.class);
        Call<ResponseBody> callLogoutApi = loginApi.setLogout(token);
        callLogoutApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    if (object.getInt("code") == 1){
                        progressDialog.dismiss();

                        GlobalFunction.toast(getActivity(), object.getString("info"));
                        GlobalFunction.addTokenAndUserRolePref(getActivity(),"default", "default");
                        GlobalFunction.moveActivity(getActivity(), MainActivity.class);
                        getActivity().finish();
                    }else {
                        progressDialog.dismiss();

                        GlobalFunction.addTokenAndUserRolePref(getActivity(),"default", "default");
                        GlobalFunction.moveActivity(getActivity(), MainActivity.class);
                        getActivity().finish();
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

    private void bindView(View view){
        homeMenu = (GridView)view.findViewById(R.id.home_menu);

    }

    private ArrayList<MenuItemModel> menuItem(){
        item = new MenuItemModel("Menu 1", R.drawable.ic_launcher_background);
        menuitem.add(item);

        item = new MenuItemModel("Menu 2", R.drawable.ic_launcher_background);
        menuitem.add(item);

        item = new MenuItemModel("Menu 3", R.drawable.ic_launcher_background);
        menuitem.add(item);

        item = new MenuItemModel("Menu 4", R.drawable.ic_launcher_background);
        menuitem.add(item);

        return menuitem;
    }

}
