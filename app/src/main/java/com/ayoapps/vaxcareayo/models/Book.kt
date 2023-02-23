package com.ayoapps.vaxcareayo.models

import java.io.Serializable

// Class to store book data
data class Book(
    val id: String,
    val title: String,
    val author: String,
    val fee: String,
    val lastEdited: String,
    val status: String
) : Serializable


