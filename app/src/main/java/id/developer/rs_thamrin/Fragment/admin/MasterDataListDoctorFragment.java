package id.developer.rs_thamrin.Fragment.admin;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.adapter.DoctorListAdapter;
import id.developer.rs_thamrin.model.master.DoctorData;

public class MasterDataListDoctorFragment extends Fragment {

    private RecyclerView recyclerView;
    private DoctorListAdapter adapter;
    private List<DoctorData> poliklinikList;
    private ProgressBar progressBar;
    private FloatingActionButton addPoliklinik;

    private FloatingActionButton addDoctor;

    public MasterDataListDoctorFragment() {
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
        View view = inflater.inflate(R.layout.fragment_master_data_list_doctor, container, false);
        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Poliklinik");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);

        addDoctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.fragment_layout_home, new MasterDataDoctorInputFragment(), "doctor_input_fragment")
                        .addToBackStack(null)
                        .commit();
            }
        });
        return view;
    }

    private void bindView(View view){
        addDoctor = view.findViewById(R.id.add_doctor);
    }
}
