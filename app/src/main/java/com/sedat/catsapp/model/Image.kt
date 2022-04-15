package com.sedat.catsapp.model

import java.io.Serializable

data class Image(
    var height: Int ?= null,
    var id: String ?= null,
    var url: String ?= null,
    var width: Int ?= null
): Serializable