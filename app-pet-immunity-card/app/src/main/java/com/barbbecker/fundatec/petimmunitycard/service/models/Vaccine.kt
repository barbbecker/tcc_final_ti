package com.barbbecker.fundatec.petimmunitycard.service.models

import java.io.Serializable

data class Vaccine(
    val id: Int?,
    val name: String,
    val nameVet: String,
    val dateApplication: String,
    val dateReapplication: String,
    val linkVaccine:String
): Serializable