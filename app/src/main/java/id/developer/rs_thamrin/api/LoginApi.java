package id.developer.rs_thamrin.api;

import id.developer.rs_thamrin.model.request.LoginRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface LoginApi {
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/login/user")
    Call<ResponseBody> setLogin(@Body LoginRequest loginRequest);

    @POST("/login/session")
    Call<ResponseBody> sessionCheck(@Query("token") String token);

    @POST("/login/logout")
    Call<ResponseBody> setLogout(@Query("token") String token);

}
