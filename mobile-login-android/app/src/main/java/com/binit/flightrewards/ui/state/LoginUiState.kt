package com.binit.flightrewards.ui.state

import com.binit.flightrewards.domain.validation.EmailValidator

data class LoginUiState(

  val email: String = "",

  val password: String = "",

  val isLoading: Boolean = false,

  val isLoggedIn: Boolean = false,

  val rememberMe: Boolean = false,

  val isOnline: Boolean = true,

  val errorMessage: String? = null,

  val failureCount: Int = 0,

  val lockoutExpiryTime: Long? = null,
) {

  val isLockedOut: Boolean
    get() = lockoutExpiryTime?.let {
      System.currentTimeMillis() < it
    } ?: false

  val isLoginEnabled: Boolean
    get() =
      EmailValidator.isValid(email)
              && password.length >= 6
              && !isLoading
              && !isLockedOut
}