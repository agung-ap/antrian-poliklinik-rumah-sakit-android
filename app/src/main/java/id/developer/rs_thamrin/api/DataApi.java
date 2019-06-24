package id.developer.rs_thamrin.api;

import id.developer.rs_thamrin.model.request.PoliklinikRequest;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataApi {
    @Headers({
            "Content-Type:application/json"
    })
    @POST("/data/input/poliklinik")
    Call<ResponseBody> setPoliklinik(@Query("token") String token,
                                     @Body PoliklinikRequest request);

    @GET("/data/userDetail")
    Call<ResponseBody> getUserDetail(@Query("token") String token,
                                     @Query("userId") String userId);

    @GET("/data/poliklinik")
    Call<ResponseBody> getPoliklinik();

    @GET("/data/dokter")
    Call<ResponseBody> getDokter();

    @GET("/data/schedule")
    Call<ResponseBody> getSchedule();

    @GET("/data/specialization")
    Call<ResponseBody> getSpecialization();

    @GET("/data/education")
    Call<ResponseBody> getEducation();

    @GET("/data/family")
    Call<ResponseBody> getFamily();

    @GET("/data/identityCard")
    Call<ResponseBody> getIndentityCard();

    @GET("/data/job")
    Call<ResponseBody> getJob();

    @GET("/data/mariedStatus")
    Call<ResponseBody> getMariedStatus();

    @GET("/data/payment")
    Call<ResponseBody> getPayment();

    @GET("/data/address")
    Call<ResponseBody> getAddress();

    @GET("/data/province")
    Call<ResponseBody> getProvince();

    @GET("/data/regency")
    Call<ResponseBody> getRegency(@Query("id") long id);

    @GET("/data/district")
    Call<ResponseBody> getDistrict(@Query("id") long id);

    @GET("/data/village")
    Call<ResponseBody> getVillage(@Query("id") long id);
    
}
