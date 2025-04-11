package com.api.services

import com.api.routing.requests.LoginRequest
import com.api.utils.verifyPassword
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.interfaces.JWTVerifier
import io.ktor.server.auth.jwt.*
import kotlinx.coroutines.runBlocking
import java.util.*

class JwtService(
    private val application: io.ktor.server.application.Application,
    private val userService: UserService
) {

    private val secret = getConfigProperty("jwt.secret")
    private val issuer = getConfigProperty("jwt.issuer")
    private val audience = getConfigProperty("jwt.audience")
    val realm = getConfigProperty("jwt.realm")

    val jwtVerifier: JWTVerifier = JWT
        .require(Algorithm.HMAC256(secret))
        .withAudience(audience)
        .withIssuer(issuer)
        .build()

    fun createJwtToken(loginRequest: LoginRequest): String? {
        val existingUser = runBlocking { userService.getUserByEmail(loginRequest.email) }

        return if (existingUser != null && verifyPassword(loginRequest.password, existingUser.password)) {

            JWT
                .create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("email", existingUser.email)
                .withClaim("userId", existingUser.id)
                .withClaim("roleId", existingUser.roleId)
                .withExpiresAt(Date(System.currentTimeMillis() + 3_600_000))
                .sign(Algorithm.HMAC256(secret))
        } else null
    }

    fun customValidator(credential: JWTCredential): JWTPrincipal? {
        val email = extractEmail(credential)
        val user = email?.let { runBlocking { userService.getUserByEmail(it) } }

        return user?.let {
            if (audienceMatches(credential)) {
                JWTPrincipal(credential.payload)
            } else null
        }
    }

    private fun audienceMatches(credential: JWTCredential): Boolean =
        credential.payload.audience.contains(audience)

    private fun extractEmail(credential: JWTCredential): String? =
        credential.payload.getClaim("email").asString()

    private fun getConfigProperty(path: String): String =
        application.environment.config.property(path).getString()
}