package com.barbbecker.fundatec.petimmunitycard.service

import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import com.barbbecker.fundatec.petimmunitycard.service.models.Vaccine
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface PetService {

    @POST("/api/pet")
    fun addPet(@Body pet: Pet): Single<ResponseBody>


    @GET("/api/pet/person/{id}")
    fun getPets(@Path("id") id:Int): Single<List<Pet>>


    @POST("/api/pet/{id}/save-vaccine")
    fun addVaccine(@Body vaccine: Vaccine,
                   @Path("id") id:Int): Single<ResponseBody>

    @GET("/api/pet/{id}/vaccine")
    fun getVaccines(@Path("id") id:Int): Single<List<Vaccine>>
}