package com.barbbecker.fundatec.petimmunitycard.service.models

data class Person(
    val id: Int?,
    val name: String,
    val address: String,
    val number: String,
    val phoneNumber: String,
    val cpf: String,
    val email: String,
    val password: String
)

{
    constructor(email: String, password: String):this(null, "", "", "",
        "", "", email, password)
}