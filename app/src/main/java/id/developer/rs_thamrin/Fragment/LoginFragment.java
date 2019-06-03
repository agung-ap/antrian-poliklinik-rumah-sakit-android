package id.developer.rs_thamrin.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import id.developer.rs_thamrin.R;

public class LoginFragment extends Fragment {
    private EditText userId;
    private EditText password;
    private Button login;
    private Button register;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        bindView(view);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_layout, new RegisterFragment(), "NewFragmentTag");
                ft.addToBackStack(null);
                ft.commit();
            }
        });

        return view;
    }

    private void bindView(View view){
        userId = (EditText)view.findViewById(R.id.userId);
        password = (EditText)view.findViewById(R.id.password);
        login = (Button)view.findViewById(R.id.login);
        register = (Button)view.findViewById(R.id.register_button);
    }

}
