package com.example.talkingwithserver;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Headers;
import retrofit2.http.Body;

public interface Retrofit_Server {
    //endpoint 1
    @GET("/users/{username}/token/")
    Call<TokenResponse> token(@Path("username") String username);

    //endpoint 2
    @GET("/user")
    Call<UserResponse> user(@Header("Authorization") String auth);

    //endpoint 3
    @Headers({"Content-Type: application/json" })
    @POST("/user/edit/")
    Call<UserResponse> edit(@Body SetUserPrettyNameRequest prettyName, @Header("Authorization") String auth);

}