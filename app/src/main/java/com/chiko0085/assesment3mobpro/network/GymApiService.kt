package com.chiko0085.assesment3mobpro.network

import com.chiko0085.assesment3mobpro.model.AlatGym
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// ⚠️ MENGGUNAKAN MOCKAPI SESUAI SCREENSHOT USER
private const val BASE_URL = "https://6a3131b17bc5e1c612655407.mockapi.io/"

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_URL)
    .build()

interface GymApiService {
    @GET("alat_gym")
    suspend fun getAlatGym(): List<AlatGym>

    @POST("alat_gym")
    suspend fun postAlatGym(@Body alatGym: AlatGym): AlatGym

    @DELETE("alat_gym/{id}")
    suspend fun deleteAlatGym(@Path("id") id: String): AlatGym
}

object GymApi {
    val service: GymApiService by lazy {
        retrofit.create(GymApiService::class.java)
    }
}

enum class ApiStatus { LOADING, SUCCESS, FAILED }
