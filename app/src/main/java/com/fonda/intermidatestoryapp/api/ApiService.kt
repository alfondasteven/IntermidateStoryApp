package com.fonda.intermidatestoryapp.api

import com.fonda.intermidatestoryapp.model.FileUploadResponse
import com.fonda.intermidatestoryapp.model.StoriesResponse
import com.fonda.intermidatestoryapp.model.UserLoginResponse
import com.fonda.intermidatestoryapp.model.UserRegisterResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("login")
    fun getLoginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserLoginResponse>

    @FormUrlEncoded
    @POST("register")
    fun createAccount(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<UserRegisterResponse>

    @GET("stories")
    fun getAllListStories(
        @Header("Authorization") authHeader: String,
    ): Call<StoriesResponse>

    @GET("stories")
    suspend fun getAllListStoriesPaging(
        @Header("Authorization") authHeader: String,
        @Query("page") page: Int
    ): StoriesResponse

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") authHeader: String,
        @Part file : MultipartBody.Part,
        @Part("description") description : RequestBody
    ): Call<FileUploadResponse>

    @GET("stories")
    fun getListStoryMaps(
        @Header("Authorization") authHeader: String,
        @Query("location") location : Int
    ) : Call<StoriesResponse>

}