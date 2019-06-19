package id.developer.rs_thamrin.Fragment.admin;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.model.response.DoctorDataResponse;
import id.developer.rs_thamrin.model.response.UserApprovalResponse;

public class DoctorInputResultFragment extends Fragment {
    private List<DoctorDataResponse> responses = new ArrayList<>();
    private Button home;
    private TextView info, name, userId, password, specialization, createdDate;

    public DoctorInputResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_doctor_input_result, container, false);
        bindView(view);


        return view;
    }

    private void bindView(View view){
        home = view.findViewById(R.id.home);

        info = view.findViewById(R.id.message);
        name = view.findViewById(R.id.name);
        userId = view.findViewById(R.id.user_id);
        password = view.findViewById(R.id.password);
        specialization = view.findViewById(R.id.specialization);
        createdDate = view.findViewById(R.id.created_date);

        info.setText(responses.get(0).getMessage());
        name.setText(responses.get(0).getName());
        userId.setText(responses.get(0).getDokterId());
        password.setText(responses.get(0).getPassword());
        specialization.setText(responses.get(0).getSpecialization());
        createdDate.setText(responses.get(0).getCreatedDate());
    }
}
