package com.itome.githubmvi.data.model

data class AccessTokenData(
    val access_token: String,
    val scope: String,
    val token_type: String
)
