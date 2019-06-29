package id.developer.rs_thamrin.api;

import id.developer.rs_thamrin.model.request.LoginRequest;
import id.developer.rs_thamrin.model.request.QueueRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface QueueApi {
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/queue/addQueue")
    Call<ResponseBody> setQueue(@Query("token") String token,@Body QueueRequest loginRequest);

    @POST("/queue/checkingPatien")
    Call<ResponseBody> callPatien(@Query("token") String token,
                                  @Query("userId") String userId);

    @DELETE("/queue/done")
    Call<ResponseBody> doneChecking(@Query("token") String token,
                                    @Query("userId") String userId);

    @GET("/queue/listQueue")
    Call<ResponseBody> getListQueue(@Query("token") String token,
                                    @Query("poliklinikCode") String poliklinikCode);

    @GET("/queue/checkQueue")
    Call<ResponseBody> checkQueue(@Query("token") String token);

    @DELETE("/queue/cancelQueue")
    Call<ResponseBody> cancelQueue(@Query("token") String token);
}
