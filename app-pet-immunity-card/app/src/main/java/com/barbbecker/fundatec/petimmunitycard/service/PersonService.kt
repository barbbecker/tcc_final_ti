package com.barbbecker.fundatec.petimmunitycard.service

import com.barbbecker.fundatec.petimmunitycard.service.models.Person
import com.barbbecker.fundatec.petimmunitycard.service.models.Success
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST

interface PersonService {

    @POST("/api/person")
    fun registerPerson(@Body person: Person): Single<ResponseBody>

    @POST("/api/login")
    fun loginPerson(@Body person: Person): Single<Success>

}