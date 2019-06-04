package id.developer.rs_thamrin.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import id.developer.rs_thamrin.R;
import id.developer.rs_thamrin.adapter.TypeOfIdentityCardAdapter;
import id.developer.rs_thamrin.api.DataApi;
import id.developer.rs_thamrin.api.RetrofitBuilder;
import id.developer.rs_thamrin.model.master.TypeOfIdentityCard;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IdentityCardResult extends AppCompatActivity implements TypeOfIdentityCardAdapter.DataListener {
    public static final int REQUEST_ADD = 10;
    public static final int REQUEST_BACK = 1;

    private static final String TAG = IdentityCardResult.class.getName();
    private RecyclerView recyclerView;
    private TypeOfIdentityCardAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type_of_identity_card);

        recyclerView = (RecyclerView)findViewById(R.id.master_data_list);
        adapter = new TypeOfIdentityCardAdapter(this , this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayout);
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    private void fetchData() {
        DataApi dataApi = RetrofitBuilder.getApiService().create(DataApi.class);
        Call<ResponseBody> callIdentityCard = dataApi.getIndentityCard();
        callIdentityCard.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    JSONObject object = new JSONObject(response.body().string());

                    ArrayList<TypeOfIdentityCard> list = new ArrayList<>();

                    if (object.getInt("code") == 0){
                        JSONArray jsonArray = object.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length();i++){
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            TypeOfIdentityCard identityCard = new TypeOfIdentityCard();

                            identityCard.setCode(jsonObject.getString("code"));
                            identityCard.setName(jsonObject.getString("name"));
                            list.add(identityCard);
                        }
                        adapter.setData(list);
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
    public void onClick(TypeOfIdentityCard dataPosition) {
        Bundle bundle = new Bundle();
        ArrayList<TypeOfIdentityCard> catalogModels = new ArrayList<>();
        catalogModels.add(dataPosition);
        //put parcelable
        bundle.putParcelableArrayList(getString(R.string.GET_SELECTED_ITEM), catalogModels);
        //send data via intent
        Intent returnIntent = new Intent();
        returnIntent.putExtras(bundle);
        setResult(REQUEST_BACK,returnIntent);
        finish();
    }

}
