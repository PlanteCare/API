package com.api.services

import com.api.repositories.UserRepository
import com.api.routing.requests.LoginRequest
import com.api.utils.verifyPassword
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import java.util.*

class JwtService (
    private val application: Application,
    private val userService: UserService
) {

    private val secret = getConfigProprety("jwt.secret")
    private val issuer = getConfigProprety("jwt.issuer")
    private val audience = getConfigProprety("jwt.audience")
    val realm = getConfigProprety("jwt.realm")

    val jwtVerifier = JWTVerifier =
        JWT
            .require(Algorithm.HMAC256(secret))
            .withAudience(audience)
            .withIssuer(issuer)
            .build()

    fun createJwtToken(loginRequest: LoginRequest): String? {
        val existingUser = userService.getUserByEmail(loginRequest.email)

        return if (existingUser != null && verifyPassword(loginRequest.password, existingUser.password)) {
            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", existingUser.email)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        } else null
    }

    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val email = extractEmail(credential)
        val user = email?.let(userService::findByEmail)

        return user?.let {
            if(audienceMatvhes(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    private fun audienceMatvhes(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

    private fun extractEmail(credential: JWTCredential) =
        credential.payload.getClaim("email").asString()


    private fun getConfigProprety(path: String) =
        application.environment.config.property(path).getString()
}