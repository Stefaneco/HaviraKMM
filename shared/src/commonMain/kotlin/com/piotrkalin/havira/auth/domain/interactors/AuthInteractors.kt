package com.piotrkalin.havira.auth.domain.interactors

data class AuthInteractors(
    val loginWithGoogle: LoginWithGoogle,
    val logout: Logout,
    val getUserProfile: GetUserProfile,
    val isUserProfileCreated: IsUserProfileCreated,
    val createUserProfile: CreateUserProfile
)
