package com.nennig.name.that.president.domain.model

data class President(
    val id: Int,
    val name: String,
    val imageAssetName: String,
    val term: String,
    val party: String
) 