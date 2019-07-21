package id.developer.rs_thamrin.Fragment.dokter;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.activity.HomeActivity;
import id.developer.rs_thamrin.adapter.QueueListAdapter;
import id.developer.rs_thamrin.api.QueueApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.response.QueueListResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QueueListFragment extends Fragment implements QueueListAdapter.Listener {
    private SharedPreferences preferences;
    private RecyclerView recyclerView;
    private QueueListAdapter adapter;
    private List<QueueListResponse> queueList;
    private ProgressBar progressBar;
    private TextView message;

    private String token, poliklinikCode;

    public QueueListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = getActivity().getSharedPreferences(getString(R.string.GET_CREDENTIAL), getContext().MODE_PRIVATE);
        poliklinikCode = preferences.getString(getString(R.string.GET_POLIKLINIK_CODE),"default");
        token = preferences.getString(getString(R.string.GET_USER_TOKEN),"default");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_queue_list, container, false);

        setHasOptionsMenu(false);
        ((HomeActivity)getActivity()).getSupportActionBar().setTitle("List Antrian");
        ((HomeActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindView(view);

        getQueueList();
        return view;
    }

    private void bindView(View view){
        adapter = new QueueListAdapter(getActivity(), this);

        recyclerView = view.findViewById(R.id.queue_list);
        message = view.findViewById(R.id.message);
        progressBar = view.findViewById(R.id.progress_bar);
        progressBar.setIndeterminate(true);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#455ede"),android.graphics.PorterDuff.Mode.MULTIPLY);
        progressBar.setVisibility(View.GONE);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }

    private void getQueueList() {
        progressBar.setVisibility(View.VISIBLE);
        message.setVisibility(View.GONE);

        QueueApi queueApi = RetrofitBuilder.getApiService().create(QueueApi.class);
        Call<ResponseBody> callQueueApi = queueApi.getListQueue(token, poliklinikCode);
        callQueueApi.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());
                    if (object.getInt("code") == 0){
                        progressBar.setVisibility(View.GONE);

                        JSONArray jsonArray = object.getJSONArray("data");
                        queueList = new ArrayList<>();

                        for (int i= 0; i<jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            QueueListResponse queueListResponse = new QueueListResponse();
                            queueListResponse.setId(jsonObject.getInt("id"));
                            queueListResponse.setPaymentType(jsonObject.getString("paymentType"));
                            queueListResponse.setPoliklinik(jsonObject.getString("poliklinik"));
                            queueListResponse.setUserId(jsonObject.getString("userID"));
                            queueListResponse.setUsername(jsonObject.getString("userName"));
                            queueList.add(queueListResponse);
                        }
                        adapter.setData(queueList);
                    }else {
                        progressBar.setVisibility(View.GONE);
                        message.setVisibility(View.VISIBLE);
                        message.setText(object.getString("info"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }



    @Override
    public void onClick(QueueListResponse dataPosition) {
        Bundle bundle = new Bundle();
        ArrayList<QueueListResponse> queueListResponses = new ArrayList<>();
        queueListResponses.add(dataPosition);
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), queueListResponses);

        QueueListDetailFragment fragment = new QueueListDetailFragment();
        fragment.setArguments(bundle);

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_layout_home, fragment, "NewFragmentTag")
                .addToBackStack(null)
                .commit();
    }
}
