package id.developer.rs_thamrin.Fragment;

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
import id.developer.rs_thamrin.model.Poliklinik;
import id.developer.rs_thamrin.model.response.QueueResponse;

public class PoliklinikRegisterResultFragment extends Fragment {
    private List<QueueResponse> responses = new ArrayList<>();

    private TextView message;
    private TextView queueCode;
    private TextView customerName;
    private TextView poliklinikName;
    private TextView doctorName;

    private Button home;


    public PoliklinikRegisterResultFragment() {
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
        View view = inflater.inflate(R.layout.fragment_poliklinik_register_result, container, false);;

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("Register Berhasil");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        bindView(view);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction trans = manager.beginTransaction();
                trans.replace(R.id.fragment_layout_home, new HomeFragment(), "home_fragment");
                trans.remove(new PoliklinikRegisterFragment());
                trans.commit();
                manager.popBackStack();

            }
        });

        return view;
    }


    private void bindView(View view){
        home = view.findViewById(R.id.back_to_home);

        message = view.findViewById(R.id.message);
        queueCode = view.findViewById(R.id.queue_code);
        customerName = view.findViewById(R.id.customer_name);
        poliklinikName = view.findViewById(R.id.poliklinik_name);
        doctorName = view.findViewById(R.id.doctor_name);

        message.setText(responses.get(0).getMessage());
        queueCode.setText(responses.get(0).getQueueCode());
        customerName.setText(responses.get(0).getCustomerName());
        poliklinikName.setText(responses.get(0).getPoliklinikName());
        doctorName.setText(responses.get(0).getDoctorName());
    }

    public void clearStack() {
        //Here we are clearing back stack fragment entries
        int backStackEntry = getFragmentManager().getBackStackEntryCount();
        if (backStackEntry > 0) {
            for (int i = 0; i < backStackEntry; i++) {
                getFragmentManager().popBackStackImmediate();
            }
        }

        //Here we are removing all the fragment that are shown here
        if (getFragmentManager().getFragments() != null && getFragmentManager().getFragments().size() > 0) {
            for (int i = 0; i < getFragmentManager().getFragments().size(); i++) {
                Fragment mFragment = getFragmentManager().getFragments().get(i);
                if (mFragment != null) {
                    getFragmentManager().beginTransaction().remove(mFragment).commit();
                }
            }
        }
    }


}
