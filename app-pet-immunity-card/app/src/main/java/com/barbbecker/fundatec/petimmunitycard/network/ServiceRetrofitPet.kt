package com.barbbecker.fundatec.petimmunitycard.network

import com.barbbecker.fundatec.petimmunitycard.service.PersonService
import com.barbbecker.fundatec.petimmunitycard.service.PetService
import com.barbbecker.fundatec.petimmunitycard.service.models.Person
import com.barbbecker.fundatec.petimmunitycard.service.models.Pet
import com.barbbecker.fundatec.petimmunitycard.service.models.Success
import com.barbbecker.fundatec.petimmunitycard.service.models.Vaccine
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.Retrofit

object ServiceRetrofitPet {
    private val retrofit: Retrofit = RetrofitInitializer["http://54.91.44.1:8080/"]

    private val instancePets: PetService by lazy {
        retrofit.create(PetService::class.java)
    }

    private val instancePerson: PersonService by lazy {
        retrofit.create(PersonService::class.java)
    }

    fun requestRegisterPet(pet: Pet): Single<ResponseBody> {
        return instancePets.addPet(pet)
    }

    fun requestGetPets(personId: Int): Single<List<Pet>> {
        return instancePets.getPets(personId)
    }

    fun requestRegisterVaccine(vaccine: Vaccine, id: Int): Single<ResponseBody> {
        return instancePets.addVaccine(vaccine, id)
    }

    fun requestGetVaccines(id: Int): Single<List<Vaccine>> {
        return instancePets.getVaccines(id)
    }

    fun requestRegisterPerson(person: Person): Single<ResponseBody> {
        return instancePerson.registerPerson(person)
    }

    fun requestLogin(person: Person): Single<Success> {
        return instancePerson.loginPerson(person)
    }
}