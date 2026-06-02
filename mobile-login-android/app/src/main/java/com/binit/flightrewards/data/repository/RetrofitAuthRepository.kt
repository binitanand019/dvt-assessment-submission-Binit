package com.binit.flightrewards.data.repository

import com.binit.flightrewards.data.remote.AuthApi
import com.binit.flightrewards.data.remote.LoginRequest
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class RetrofitAuthRepository @Inject constructor(
    private val authApi: AuthApi
) : AuthRepository {

    override suspend fun login(
        email: String,
        password: String
    ): String {

        try {

            val response =
                authApi.login(
                    LoginRequest(
                        email = email,
                        password = password
                    )
                )

            return response.token

        } catch (e: HttpException) {

            throw AuthException(
                "Invalid credentials"
            )

        } catch (e: IOException) {

            throw IOException(
                "Network error"
            )
        }
    }
}