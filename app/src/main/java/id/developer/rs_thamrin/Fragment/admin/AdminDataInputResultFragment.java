package id.developer.rs_thamrin.Fragment.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.model.response.AdminRegisterResponse;
import id.developer.rs_thamrin.model.response.DoctorDataResponse;

public class AdminDataInputResultFragment extends Fragment {
    private List<AdminRegisterResponse> responses = new ArrayList<>();
    private Button home;
    private TextView info, name, userId, password, createdDate;

    public AdminDataInputResultFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        responses = getArguments().getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_admin_data_input_result, container, false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Success");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        bindView(view);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();

                if (manager.getBackStackEntryCount() > 0) {
                    FragmentManager.BackStackEntry first = manager.getBackStackEntryAt(1);
                    manager.popBackStack(first.getId(), FragmentManager.POP_BACK_STACK_INCLUSIVE);
                }

                trans.remove(new MasterDataDoctorInputFragment());
                trans.commit();
            }
        });

        return view;
    }

    private void bindView(View view){
        home = view.findViewById(R.id.home);

        info = view.findViewById(R.id.message);
        name = view.findViewById(R.id.name);
        userId = view.findViewById(R.id.user_id);
        password = view.findViewById(R.id.password);
        createdDate = view.findViewById(R.id.created_date);

        info.setText(responses.get(0).getInfo());
        name.setText(responses.get(0).getName());
        userId.setText(responses.get(0).getAdminId());
        password.setText(responses.get(0).getPassword());
        createdDate.setText(responses.get(0).getCreatedDate());
    }
}
