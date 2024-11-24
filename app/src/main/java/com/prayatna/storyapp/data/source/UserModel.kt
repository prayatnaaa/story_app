package com.prayatna.storyapp.data.source

data class UserModel(
    val email: String,
    val password: String,
    val token: String,
    val isLogin: Boolean = false
)
