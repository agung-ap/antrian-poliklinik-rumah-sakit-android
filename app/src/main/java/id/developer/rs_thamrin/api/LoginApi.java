package id.developer.rs_thamrin.api;

import id.developer.rs_thamrin.model.request.ChangePasswordRequest;
import id.developer.rs_thamrin.model.request.LoginRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;

public interface LoginApi {
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/login/user")
    Call<ResponseBody> setLogin(@Body LoginRequest loginRequest,
                                @Query("deviceToken") String deviceToken);

    @POST("/login/session")
    Call<ResponseBody> sessionCheck(@Query("token") String token,
                                    @Query("deviceToken") String deviceToken);

    @POST("/login/logout")
    Call<ResponseBody> setLogout(@Query("token") String token);

    @Headers({
            "Content-Type:application/json"
    })
    @PUT("/login/changePassword")
    Call<ResponseBody> changePassword(@Query("token") String token,
                                      @Body ChangePasswordRequest request);

}
