package id.developer.rs_thamrin.Fragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.result.FamilyResult;
import id.developer.rs_thamrin.activity.result.IdentityCardResult;
import id.developer.rs_thamrin.activity.MainActivity;
import id.developer.rs_thamrin.model.master.Family;
import id.developer.rs_thamrin.model.master.TypeOfIdentityCard;

public class RegisterFragment extends Fragment implements View.OnClickListener{
    private EditText firstName;
    private EditText lastName;
    private EditText birthPlace;
    private EditText birthDate;
    private EditText family;
    private EditText typeOfFamily;
    private EditText identityCard;
    private EditText typeOfIdentityCard;
    private Spinner gender;
    private EditText mariedStatus;
    private EditText education;
    private EditText phoneNumber;
    private EditText job;
    private EditText typeOfPayment;
    private EditText country;
    private EditText province;
    private EditText regency;
    private EditText district;
    private EditText village;
    private EditText address;
    private EditText typeOfAddress;

    private String familyCode;
    private String identitycardCode;
    private String mariedStatusCode;
    private String educationCode;
    private String jobCode;
    private String typeOfPaymentCode;
    private String provinceCode;
    private String regencyCode;
    private String districtCode;
    private String villageCode;
    private String typeOfAddressCode;

    private Calendar myCalendar = Calendar.getInstance();

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        ((MainActivity)getActivity()).getSupportActionBar().setTitle("Register");
        ((MainActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        bindView(view);

        birthDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        typeOfIdentityCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), IdentityCardResult.class);
                startActivityForResult(intent, IdentityCardResult.REQUEST_ADD);
            }
        });

        return view;
    }

    private void bindView(View view) {
        firstName = (EditText)view.findViewById(R.id.first_name);
        lastName = (EditText)view.findViewById(R.id.last_name);
        birthPlace = (EditText)view.findViewById(R.id.birth_place);
        birthDate = (EditText)view.findViewById(R.id.birth_date);
        family = (EditText)view.findViewById(R.id.family_name);
        typeOfFamily = (EditText)view.findViewById(R.id.type_of_family_name);
        identityCard = (EditText)view.findViewById(R.id.identity_card);
        typeOfIdentityCard = (EditText)view.findViewById(R.id.type_of_identity_card);
        gender = (Spinner) view.findViewById(R.id.gender);
        mariedStatus = (EditText)view.findViewById(R.id.maried_status);
        education = (EditText)view.findViewById(R.id.education);
        phoneNumber = (EditText)view.findViewById(R.id.phone_number);
        job = (EditText)view.findViewById(R.id.job);
        typeOfPayment = (EditText)view.findViewById(R.id.type_of_payment);
        country = (EditText)view.findViewById(R.id.country);
        province = (EditText)view.findViewById(R.id.province);
        regency = (EditText)view.findViewById(R.id.regency);
        district = (EditText)view.findViewById(R.id.district);
        village = (EditText)view.findViewById(R.id.village);
        address = (EditText)view.findViewById(R.id.address);
        typeOfAddress = (EditText)view.findViewById(R.id.type_of_address);
    }

    final DatePickerDialog.OnDateSetListener mdate = new DatePickerDialog.OnDateSetListener(){
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            birthDate.setText(sdf.format(myCalendar.getTime()));
        }
    };

    public void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), mdate, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == IdentityCardResult.REQUEST_BACK){

            Bundle bundle = data.getExtras();
            ArrayList<TypeOfIdentityCard> list = new ArrayList<>();
            list = bundle.getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));

            identitycardCode = list.get(0).getCode();
            typeOfIdentityCard.setText(list.get(0).getName());
        }
        if (resultCode == FamilyResult.REQUEST_BACK){
            Bundle bundle = data.getExtras();
            ArrayList<Family> list = new ArrayList<>();
            list = bundle.getParcelableArrayList(getString(R.string.GET_SELECTED_ITEM));

            familyCode = list.get(0).getCode();
            typeOfFamily.setText(list.get(0).getName());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.birth_date:
                showDatePickerDialog();
                break;
            case R.id.type_of_identity_card:
                startActivityForResult(new Intent(getActivity(), IdentityCardResult.class),
                        IdentityCardResult.REQUEST_ADD);
                break;
            case R.id.type_of_family_name:
                startActivityForResult(new Intent(getActivity(), FamilyResult.class),
                        FamilyResult.REQUEST_ADD);
                break;
        }

    }
}
