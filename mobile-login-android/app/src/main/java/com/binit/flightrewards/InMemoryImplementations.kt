package com.binit.flightrewards

import com.binit.flightrewards.data.network.NetworkMonitor
import com.binit.flightrewards.data.repository.AuthException
import com.binit.flightrewards.data.repository.AuthRepository
import com.binit.flightrewards.data.storage.TokenStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class InMemoryNetworkMonitor(
  isOnline: Boolean
) : NetworkMonitor {

  private val _online =
    MutableStateFlow(isOnline)

  override val isOnline: StateFlow<Boolean> =
    _online

  fun setOnline(value: Boolean) {
    _online.value = value
  }
}

class InMemoryTokenStore : TokenStore {

  private var token: String? = null

  override suspend fun saveToken(
    token: String
  ) {
    this.token = token
  }

  override suspend fun clearToken() {
    token = null
  }

  override suspend fun readToken(): String? {
    return token
  }
}

/**
 * Fake repository for local development,
 * UI previews and tests only.
 *
 * Valid credentials:
 * Email: user@example.com
 * Password: Password1
 */
class FakeAuthRepository(
  private val tokenStore: TokenStore
) : AuthRepository {

  override suspend fun login(
    email: String,
    password: String
  ): String {

    if (
      email == "user@example.com" &&
      password == "Password1"
    ) {
      return "token-abc"
    }

    throw AuthException(
      "Invalid credentials"
    )
  }
}