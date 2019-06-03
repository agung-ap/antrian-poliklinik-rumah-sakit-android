package id.developer.rs_thamrin.api;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DataApi {
    @POST("/data/inputPoliklinik")
    Call<ResponseBody> setPoliklinik(@Query("token") String token,
                                     @Field("code") String email,
                                     @Field("schedule") String schedule,
                                     @Field("doctor") String doctorId,
                                     @Field("specialization") String specialization,
                                     @Field("kuota") int kuota);

    @GET("/data/userDetail")
    Call<ResponseBody> getUserDetail(@Query("token") String token,
                                     @Query("userId") String userId);

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

    @GET("/data/province")
    Call<ResponseBody> getProvince();

    @GET("/data/regency")
    Call<ResponseBody> getRegency(@Query("provinceId") long id,@Query("page") int page );

    @GET("/data/district")
    Call<ResponseBody> getDistrict(@Query("regencyId") long id,@Query("page") int page );

    @GET("/data/village")
    Call<ResponseBody> getVillage(@Query("districtId") long id,@Query("page") int page );
    
}
