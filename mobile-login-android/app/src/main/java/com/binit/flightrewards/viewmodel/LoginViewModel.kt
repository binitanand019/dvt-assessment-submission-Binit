package com.binit.flightrewards.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.binit.flightrewards.data.network.NetworkMonitor
import com.binit.flightrewards.data.repository.AuthException
import com.binit.flightrewards.data.repository.AuthRepository
import com.binit.flightrewards.data.storage.TokenStore
import com.binit.flightrewards.domain.validation.EmailValidator
import com.binit.flightrewards.ui.state.LoginUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

sealed interface LoginEvent {
  data object NavigateToHome : LoginEvent
}

@HiltViewModel
class LoginViewModel @Inject constructor(
  private val authRepository: AuthRepository,
  private val networkMonitor: NetworkMonitor,
  private val tokenStore: TokenStore,
) : ViewModel() {

  private val _uiState = MutableStateFlow(LoginUiState())
  val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

  private val _isLoggedIn = MutableStateFlow(false)
  val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

  private val eventsChannel = Channel<LoginEvent>(capacity = Channel.BUFFERED)
  val events = eventsChannel.receiveAsFlow()

  init {

    viewModelScope.launch {
      _isLoggedIn.value = tokenStore.readToken() != null
    }

    viewModelScope.launch {
      networkMonitor.isOnline.collect { online ->
        _uiState.update {
          it.copy(isOnline = online)
        }
      }
    }
  }

  fun onEmailChanged(value: String) {
    _uiState.update {
      it.copy(
        email = value,
        errorMessage = null
      )
    }
  }

  fun onPasswordChanged(value: String) {
    _uiState.update {
      it.copy(
        password = value,
        errorMessage = null
      )
    }
  }

  fun onRememberMeChanged(value: Boolean) {
    _uiState.update {
      it.copy(
        rememberMe = value
      )
    }
  }

  fun onLoginClicked() {

    val snapshot = _uiState.value

    val sanitizedEmail = snapshot.email.trim()
    val sanitizedPassword = snapshot.password.trim()

    if (!EmailValidator.isValid(sanitizedEmail)
      || sanitizedPassword.length < 6
    ) {

      _uiState.update {
        it.copy(
          errorMessage = "Please enter a valid email and password"
        )
      }

      return
    }

    if (!snapshot.isOnline) {

      _uiState.update {
        it.copy(
          errorMessage = "You appear to be offline"
        )
      }

      return
    }

    if (snapshot.isLockedOut) {

      _uiState.update {
        it.copy(
          errorMessage = "Account temporarily locked"
        )
      }

      return
    }

    _uiState.update {
      it.copy(
        isLoading = true,
        errorMessage = null
      )
    }

    viewModelScope.launch {

      try {

        // Fake API delay for production-like UX
        delay(2000)

        val token = authRepository.login(
          sanitizedEmail,
          sanitizedPassword
        )

        if (snapshot.rememberMe) {

          tokenStore.saveToken(token)

        } else {

          tokenStore.clearToken()
        }

        _uiState.update {
          it.copy(
            isLoading = false,
            failureCount = 0,
            lockoutExpiryTime = null
          )
        }

        _isLoggedIn.value = true

        eventsChannel.trySend(LoginEvent.NavigateToHome)

      } catch (e: Throwable) {

        val nextFailureCount =
          _uiState.value.failureCount + 1

        val lockoutExpiry =
          if (nextFailureCount >= 3) {
            System.currentTimeMillis() + (5 * 60 * 1000)
          } else {
            null
          }

        val errorMessage = when {

          lockoutExpiry != null ->
            "Too many failed attempts. Try again in 5 minutes."

          e is AuthException ->
            e.message ?: "Invalid credentials"

          e is UnknownHostException ->
            "Unable to reach server. Please check your connection."

          e is SocketTimeoutException ->
            "Request timed out. Please try again."

          e is IOException ->
            "Network error. Please check your connection."

          else ->
            "Login failed. Please try again."
        }

        _uiState.update {
          it.copy(
            isLoading = false,
            failureCount = nextFailureCount,
            lockoutExpiryTime = lockoutExpiry,
            errorMessage = errorMessage,
          )
        }
      }
    }
  }

  fun logout() {

    viewModelScope.launch {

      tokenStore.clearToken()

      _isLoggedIn.value = false

      val online = _uiState.value.isOnline

      _uiState.value =
        LoginUiState(isOnline = online)
    }
  }

  fun resetForLogout() {

    val online = _uiState.value.isOnline

    _uiState.value =
      LoginUiState(isOnline = online)
  }
}