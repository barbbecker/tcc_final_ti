package com.barbbecker.fundatec.petimmunitycard.service.models

import java.io.Serializable

data class Pet(
    val birthDate: String,
    val breed: String,
    val linkImage: String,
    val id: Int?,
    val name: String,
    val numberChip: String,
    val cpf: String,
    val vaccine:List<Vaccine>
): Serializable