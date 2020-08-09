package com.example.tweeter_v1

import io.reactivex.Observable
import retrofit2.http.GET

interface INetworkAPI {
    @GET("posts/")
    fun getAllPosts(): Observable<List<VerifyClassification.DBWrite>>

}