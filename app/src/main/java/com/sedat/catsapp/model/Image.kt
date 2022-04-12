package com.sedat.catsapp.model

import java.io.Serializable

data class Image(
    val height: Int ?= null,
    val id: String ?= null,
    val url: String ?= null,
    val width: Int ?= null
): Serializable