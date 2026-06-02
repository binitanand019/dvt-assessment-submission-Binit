package com.binit.flightrewards.data.repository

import android.util.Log
import com.binit.flightrewards.data.remote.AuthApi
import com.binit.flightrewards.data.remote.LoginRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val api: AuthApi
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): String {

        try {

            val response = api.login(
                LoginRequest(
                    email = email,
                    password = password
                )
            )

            return response.token

        } catch (e: HttpException) {

            throw AuthException("Invalid credentials")

        } catch (e: IOException) {

            throw IOException("Network error")

        } catch (e: Exception) {

            throw Exception("Login failed")
        }
    }
}