package rest;

import java.util.List;


import Model.Doctor;
import Model.Patient;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface Api {

    String BASE_URL = "http://192.168.43.197:8080/";
    //http://100.107.112.54:1010/
   // String BASE_URL = "http://wellnessq.mybluemix.net/";

    @GET("doctor/getavailabledoctors/{category}/{slot}")
    Call<List<Doctor>> getAvailableDocs(@Path("category") String category, @Path("slot") String slot);


    @GET("doctor/getDoctorQueue/{phone}/{slot}")
    Call<Integer> getDoctorQueue(@Path("phone") String phone, @Path("slot") String slot);

    @PUT("doctor/queueminusdoctor/{slot}")
    Call<Integer> decrementQueue(@Path("slot") String slot,@Body Doctor d);

    @GET("patient/getmyToken/{phone}")
    Call<Integer> getPatientToken(@Path("phone") String phone);

    @PUT("doctor/queueplusdoctor/{slot}/{patientPhoneNumber}")
    Call<Integer> incrementQueue(@Path("slot") String slot,@Path("patientPhoneNumber") String pPhone,@Body Doctor d);

   @POST("doctor/saveDoctor")
   Call<Doctor> saveDoctor(@Body Doctor d);


 @POST("patient/savepatient")
 Call<Void> savePatient(@Body Patient p);



/*
@PathVariable String slot, @PathVariable String patientPhoneNumber,
			@RequestBody Doctor doc
    @GET("consumer/getConsumers")
    Call<List<Doctor>> getConsumers();

    @POST("consumer/loginConsumer")
    Call<String> loginConsumer(@Body Doctor c);

   @POST("consumer/loginConsumer")
   Call<String> loginMerchant(@Body Doctor c);

    @GET("business/getallbusiness")
    Call<List<Doctor>> getAllBusiness();
    */

}
