package id.developer.rs_thamrin.Fragment.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.Fragment.admin.PoliklinikInputFragment;
import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.model.Poliklinik;
import id.developer.rs_thamrin.model.response.QueueResponse;
import id.developer.rs_thamrin.util.ConstantUtil;


public class PoliklinikDetailFragment extends Fragment {
    private SharedPreferences preferences;
    private String userRole;

    private ArrayList<Poliklinik> poliklinikList = new ArrayList<>();
    private TextView title ;
    private TextView time;
    private TextView doctorName;
    private TextView kuota;

    public PoliklinikDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        userRole = preferences.getString(getString(R.string.GET_USER_ROLE),"default");
        poliklinikList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_poliklinik_detail, container, false);

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle(poliklinikList.get(0).getPoliklinikName());
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);

        bindView(view);
        setData();

        return view;
    }

    private void bindView(View view){
        title = (TextView)view.findViewById(R.id.title_detail);
        time = (TextView)view.findViewById(R.id.time_detail);
        doctorName = (TextView)view.findViewById(R.id.doctor_name_detail);
        kuota = (TextView)view.findViewById(R.id.kuota_detail);
    }

    private void setData(){
        title.setText(poliklinikList.get(0).getPoliklinikName());
        time.setText(poliklinikList.get(0).getTime());
        doctorName.setText(poliklinikList.get(0).getDoctorName());
        kuota.setText(String.valueOf(poliklinikList.get(0).getKuota()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (userRole.equals("ADMIN")){
            if (menu != null){
                menu.clear();
            }

            inflater.inflate(R.menu.list_menu, menu);
            super.onCreateOptionsMenu(menu, inflater);
        }else {
            if (menu != null){
                menu.clear();
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.edit_menu :
                sendResponseToAnotherFragment(poliklinikList);
                break;
            case R.id.delete_menu :

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendResponseToAnotherFragment(ArrayList<Poliklinik> response){
        Bundle bundle = new Bundle();
        bundle.putBoolean("isEdit", true);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), response);

        PoliklinikInputFragment fragment = new PoliklinikInputFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "poliklinik_register_result_fragment")
                .addToBackStack(null)
                .commit();
    }
}
