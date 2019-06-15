package id.developer.rs_thamrin.Fragment;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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


public class HomeFragment extends Fragment implements HomeAdapter.Listener{
    private static final String TAG = HomeFragment.class.getName();

    private SharedPreferences preferences;
    private String userRole;
    private String token;

    private RecyclerView homeMenu;
    private ArrayList<MenuItemModel> menuitem;
    private HomeAdapter adapter;
    private MenuItemModel item;

    private ProgressDialog progressDialog;

    private String[] userTitle = {"Pendaftaran Poliklinik", "Lihat Poliklinik"};
    private int[] userIcon = {R.drawable.ic_poliklinik_regis, R.drawable.ic_poliklinik_list};

    public HomeFragment() {

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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Beranda");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        setHasOptionsMenu(true);

        bindView(view);

        if (userRole.equals("default")){
            Toast.makeText(getActivity(), "Silahkan Login terlebih dahulu", Toast.LENGTH_SHORT).show();
            getActivity().finish();
        }

        if (userRole.equals("USER")){
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getActivity(),this);
            adapter.setData(menuItem(userTitle,userIcon));
            homeMenu.setAdapter(adapter);


        }
        if (userRole.equals("ADMIN")){
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getActivity(),this);
            adapter.setData(menuItem(userTitle,userIcon));
            homeMenu.setAdapter(adapter);

        }
        if (userRole.equals("DOKTER")){
            menuitem = new ArrayList<>();
            adapter = new HomeAdapter(getActivity(),this);
            adapter.setData(menuItem(userTitle,userIcon));
            homeMenu.setAdapter(adapter);

        }
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (menu != null){
            menu.clear();
        }

        inflater.inflate(R.menu.home_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
        homeMenu = (RecyclerView) view.findViewById(R.id.home_menu);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
        homeMenu.setLayoutManager(gridLayoutManager);
    }

    private ArrayList<MenuItemModel> menuItem(String[] title, int[] image){

        for (int i = 0; i < image.length; i++){
           item = new MenuItemModel(i,title[i], image[i]);
           menuitem.add(item);
        }

        return menuitem;
    }

    @Override
    public void onClick(MenuItemModel dataPosition) {
        switch (dataPosition.getId()){
            case 0:
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, new PoliklinikRegisterFragment(), "poliklinik_register_fragment")
                        .addToBackStack(null)
                        .commit();
                break;
            case 1:
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, new PoliklinikListFragment(), "poliklinik_list_fragment")
                        .addToBackStack(null)
                        .commit();
                break;
        }

    }


}
