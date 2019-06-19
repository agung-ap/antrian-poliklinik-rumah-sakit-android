package id.developer.rs_thamrin.Fragment.user;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.model.Poliklinik;


public class PoliklinikDetailFragment extends Fragment {
    private List<Poliklinik> poliklinikList = new ArrayList<>();
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

}
