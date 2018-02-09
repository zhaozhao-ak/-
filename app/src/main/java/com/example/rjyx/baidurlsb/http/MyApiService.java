package com.example.rjyx.baidurlsb.http;

import com.google.gson.JsonElement;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by silver on 16-6-7.
 * blog:http://blog.csdn.net/vfush
 */
public interface MyApiService {

    /**
     * 获取所有患者信息
     */
    @GET("Patient/GetPatientList")
    Call<String> getUsers(@Query("LastPatientId") int LastPatientId);

    /**
     * 根据ID获取患者图片
     */
    @GET("Patient/GetImagesByPatientId")
    Call<JsonElement> getPatientPic(@Query("PatientId") int PatientId);

    /**
     * 上传图片
     */
    @Multipart
    @POST("UpLoad/UpLoadFile")
    Call<JsonElement> postUploadPic(@QueryMap Map<String, Object> options, @Part MultipartBody.Part file);


    /**
     * 上传多张图片
     */
    @Multipart
    @POST("UpLoad/UpLoadFile")
    Call<JsonElement> uploadManyImage(@QueryMap Map<String, Object> options, @Part() MultipartBody.Part[] file);

    /**
     * 上传多张图片
     */
    @Multipart
    @POST("UpLoad/UpLoadFile")
    Call<JsonElement> upload(@QueryMap Map<String, Object> options, @PartMap Map<String, RequestBody> params);

    /**
     * 删除图片
     */
    @Headers({"Content-type:application/json"})
    @POST("Patient/DeletePhoto")
    Call<JsonElement> postDeletePic(@Body RequestBody requestBody);


    /**
     * 查询检验记录
     */
    @Headers({"Content-type:application/json"})
    @GET("/api/InspectionRecords/GetInspectionRecordsList")
    Call<JsonElement>getInspectionRecordsByPatientId(@Query("PatientId") int DialysisId, @Query("Page") int page);

    /**
     * 查询住院信息
     */
    @Headers({"Content-type:application/json"})
    @GET("/api/Hospitalization/GetHospitalizationList")
    Call<JsonElement>getHospitalizationListByPatientId(@Query("PatientId") int DialysisId, @Query("Page") int page);

    /**
     * 获取护理（罗湖）
     */
    @Headers({"Content-type:application/json"})
    @GET("/api/HisLis_Orders/GetHisLis_OrderNursingsList")
    Call<JsonElement>getGetHisLis_OrderNursingsList(@Query("PatientId") int PatientId, @Query("Start") String Start, @Query("End") String End);


    /**
     * 获取药品（罗湖）
     */
    @Headers({"Content-type:application/json"})
    @GET("/api/HisLis_Orders/GetHisLis_OrderDrugsList")
    Call<JsonElement>getGetHisLis_OrderDrugsList(@Query("PatientId") int PatientId, @Query("Start") String Start, @Query("End") String End);



    /**
     * 上传多脱水，少脱水状态
     */
    @Headers({"Content-type:application/json"})
    @POST("DehydrationState/UpdateDehydrationState")
    Call<JsonElement> postDehydration(@Body RequestBody requestBody);



    /**
     * 获取透析详情页
     */
    @GET("/api/DialysisViewDetailWithCache/DialysisViewDetail")
    Call<JsonElement> getDialysisViewDetail(@Query("DialysisId") int DialysisId);





    /**
     * 获取AccessToken
     */
    @Headers({"Content-type:application/json"})
    @GET("oauth/2.0/token")
    Call<JsonElement>getAccessToken(@Query("grant_type") String grant_type, @Query("client_id") String client_id, @Query("client_secret") String client_secret);


    /**
     * 识别人脸
     */
    @Headers({"Content-type:application/x-www-form-urlencoded"})
    @GET("rest/2.0/face/v2/match")
    Call<JsonElement> getFace(@Query("access_token") String access_token, @Query("images") String images);



}
