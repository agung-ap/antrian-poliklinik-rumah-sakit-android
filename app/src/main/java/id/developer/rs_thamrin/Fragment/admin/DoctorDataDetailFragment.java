package id.developer.rs_thamrin.Fragment.admin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.model.Poliklinik;
import id.developer.rs_thamrin.model.master.DoctorData;

public class DoctorDataDetailFragment extends Fragment {
    private List<DoctorData> doctorDataList = new ArrayList<>();
    private TextView name ;
    private TextView specialization;
    private TextView address;

    public DoctorDataDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        doctorDataList = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_doctor_data_detail, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Detail");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
}
