package id.developer.rs_thamrin.Fragment;

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
import id.developer.rs_thamrin.activity.MainActivity;
import id.developer.rs_thamrin.model.response.CustomerRegisterResponse;

public class RegisterFragmentResult extends Fragment {
    private List<CustomerRegisterResponse> responses = new ArrayList<>();

    private TextView message;
    private TextView rmId;
    private TextView password;
    private TextView customerName;
    private TextView createdDate;

    private Button home;

    public RegisterFragmentResult() {
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
        View view = inflater.inflate(R.layout.fragment_register_result, container, false);
        setHasOptionsMenu(false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register Berhasil");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        bindView(view);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.fragment_layout, new LoginFragment(), "login_fragment");
                trans.remove(new RegisterFragment());
                trans.commit();
                manager.popBackStack();
            }
        });

        return view;
    }

    private void bindView(View view){
        home = view.findViewById(R.id.customer_home_result);

        message = view.findViewById(R.id.customer_message_result);
        rmId = view.findViewById(R.id.customer_rm_id_result);
        password = view.findViewById(R.id.customer_password_result);
        customerName = view.findViewById(R.id.customer_name_result);
        createdDate = view.findViewById(R.id.customer_created_date_result);

        message.setText(responses.get(0).getInfo());
        rmId.setText(responses.get(0).getRmId());
        password.setText(responses.get(0).getPassword());
        customerName.setText(responses.get(0).getName());
        createdDate.setText("Tanggal registrasi " + responses.get(0).getCreatedDate());
    }
}
