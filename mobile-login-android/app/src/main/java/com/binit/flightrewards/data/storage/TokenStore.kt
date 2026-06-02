package com.binit.flightrewards.data.storage

/**
 * Persistence abstraction for Remember Me.
 *
 * In a real app you might back this with DataStore/EncryptedSharedPreferences/Keychain.
 */
interface TokenStore {
  suspend fun saveToken(token: String)
  suspend fun clearToken()
  suspend fun readToken(): String?
}