package id.developer.rs_thamrin.api;

import id.developer.rs_thamrin.model.request.LoginRequest;
import id.developer.rs_thamrin.model.request.UserApproveRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface RegisterApi {
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/register/approve")
    Call<ResponseBody> setApprove(@Body UserApproveRequest approveRequest);
}
